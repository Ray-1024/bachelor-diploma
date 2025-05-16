package ray1024.submissiontestingworker.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ray1024.submissiontestingworker.model.entity.Submission;
import ray1024.submissiontestingworker.model.entity.SubmissionStatus;
import ray1024.submissiontestingworker.model.entity.TestCase;
import ray1024.submissiontestingworker.repository.SubmissionRepository;
import ray1024.submissiontestingworker.repository.SubmissionStatusRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@AllArgsConstructor
@Service
public class SubmissionService {
    private static final String WORKDIR = "/submissions/%s/";
    private static final String WORKDIR_FILE = WORKDIR + "%s";
    private static final String WORKDIR_REMOVE_COMMAND = "rm -rf " + WORKDIR;

    private static final String APPARMOR_PROFILE_FILE = "/etc/apparmor.d/%s";
    private static final String APPARMOR_GENERATE_PROFILE_COMMAND = "aa-genprof %s";
    private static final String APPARMOR_DEACTIVATE_PROFILE_COMMAND = "aa-disable %s";
    private static final String APPARMOR_REMOVE_PROFILE_COMMAND = "rm " + APPARMOR_PROFILE_FILE;

    private static final String INPUT_FILE = "input.txt";
    private static final String OUTPUT_FILE = "output.txt";
    private static final String STREAMS_REDIRECTING = "%s>&0 &1>%s &2>/dev/null".formatted(INPUT_FILE, OUTPUT_FILE);

    private static final String CPP_SOURCE_CODE_FILE = "main.cpp";
    private static final String CPP_PROGRAM_FILE = "main";
    private static final String CPP_COMPILE_COMMAND = "g++ -o %s %s";
    private static final String CPP_RUN_COMMAND = "./%s " + STREAMS_REDIRECTING;

    private static final String JAVA_SOURCE_CODE_FILE = "Main.java";
    private static final String JAVA_RUN_COMMAND = "java %s " + STREAMS_REDIRECTING;

    private final SubmissionRepository submissionRepository;
    private final SubmissionStatusRepository submissionStatusRepository;

    public void dropLongTestingSubmissions() {
        submissionRepository.dropLongTestingSubmissions(Instant.now().minus(30, ChronoUnit.SECONDS));
    }

    private void changeStatus(Submission submission, SubmissionStatus.Status statusEnum) {
        SubmissionStatus status = submissionStatusRepository.findByStatus(statusEnum.name()).orElseThrow(RuntimeException::new);
        submission.setStatus(status);
        submission.setLastStatusChanged(Instant.now());
        submissionRepository.save(submission);
    }

    private void createWorkDirectory(Submission submission) {
        try {
            new File(WORKDIR.formatted(submission.getId().toString())).mkdirs();
        } catch (Exception e) {
            changeStatus(submission, SubmissionStatus.Status.RUNTIME_ERROR);
        }
    }

    private void cleanUp(Submission submission) {
        try {
            Runtime.getRuntime().exec(new String[]{
                    WORKDIR_REMOVE_COMMAND.formatted(submission.getId().toString()),
            });
        } catch (Exception exception) {
            changeStatus(submission, SubmissionStatus.Status.RUNTIME_ERROR);
        }
    }

    private void compileLimitsActivate(Submission submission) {

    }

    private void compileLimitsDeactivate(Submission submission) {

    }

    private void compile(Submission submission) {
        try {
            compileLimitsActivate(submission);
            switch (submission.getLanguage().getLanguage()) {
                case "CPP" -> {
                    Files.writeString(Path.of(WORKDIR_FILE.formatted(submission.getId().toString(), CPP_SOURCE_CODE_FILE)), submission.getSourceCode());
                    Runtime.getRuntime().exec(new String[]{
                            CPP_COMPILE_COMMAND.formatted(workDirFile(submission, CPP_PROGRAM_FILE), workDirFile(submission, CPP_SOURCE_CODE_FILE))
                    });
                }
                case "JAVA" ->
                        Files.writeString(Path.of(workDirFile(submission, JAVA_SOURCE_CODE_FILE)), submission.getSourceCode());
                default -> throw new UnsupportedOperationException("Unsupported language: " + submission.getLanguage());
            }
            compileLimitsDeactivate(submission);
        } catch (Exception ignored) {
            changeStatus(submission, SubmissionStatus.Status.COMPILE_ERROR);
        }
    }

    private String cppAppArmorTestConfig(Submission submission) {
        return new StringBuilder()
                .append("abi <abi/3.0>,").append('\n')
                .append("include <tunables/global>").append('\n')
                .append(workDirFile(submission, CPP_PROGRAM_FILE)).append(" {\n")
                .append("\tinclude <abstractions/base>").append('\n')
                .append("\tinclude <abstractions/bash>").append('\n')
                .append('\t').append(workDirFile(submission, CPP_PROGRAM_FILE)).append(" ix,\n")
                .append('\t').append(workDirFile(submission, INPUT_FILE)).append(" r,\n")
                .append('\t').append(workDirFile(submission, OUTPUT_FILE)).append(" w,\n")
                .append("\t}\n")
                .toString();
    }

    private void testLimitsActivate(Submission submission) throws IOException {
        Runtime.getRuntime().exec(new String[]{
                "aa-genprof " + workDirFile(submission, JAVA_SOURCE_CODE_FILE)
        });
        Files.writeString(Path.of(workDirFile(submission, JAVA_SOURCE_CODE_FILE).replace('/', '.')), submission.getSourceCode());

    }

    private void testLimitsDeactivate(Submission submission) {

    }

    private void test(Submission submission) {
        try {
            testLimitsActivate(submission);
            SubmissionStatus.Status status = null;
            for (TestCase aCase : submission.getProblem().getTests()) {
                Files.writeString(Path.of(workDirFile(submission, INPUT_FILE)), aCase.getInput());

                switch (submission.getLanguage().getLanguage()) {
                    case "CPP" -> Runtime.getRuntime().exec(new String[]{
                            CPP_RUN_COMMAND.formatted(workDirFile(submission, CPP_PROGRAM_FILE))
                    });
                    case "JAVA" -> Runtime.getRuntime().exec(new String[]{
                            JAVA_RUN_COMMAND.formatted(workDirFile(submission, JAVA_SOURCE_CODE_FILE))
                    });
                    default ->
                            throw new UnsupportedOperationException("Unsupported language: " + submission.getLanguage());
                }
                String outputTxt;
                outputTxt = Files.readString(Path.of(workDirFile(submission, OUTPUT_FILE)));
                if (!Objects.equals(outputTxt, aCase.getOutput())) {
                    status = SubmissionStatus.Status.WRONG_ANSWER;
                    break;
                }
            }
            changeStatus(submission, Objects.isNull(status) ? SubmissionStatus.Status.OK : status);
            testLimitsDeactivate(submission);
        } catch (Exception exception) {
            changeStatus(submission, SubmissionStatus.Status.RUNTIME_ERROR);
        }
    }


    public void reserveAndTestSubmission() {
        submissionRepository.reserveSubmission().ifPresent(submission -> {
            cleanUp(submission);
            createWorkDirectory(submission);
            compile(submission);
            test(submission);
            cleanUp(submission);
        });
    }
}
