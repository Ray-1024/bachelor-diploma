package ray1024.submissiontestingworker.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ray1024.submissiontestingworker.model.entity.Submission;
import ray1024.submissiontestingworker.model.entity.SubmissionStatus;
import ray1024.submissiontestingworker.model.entity.TestCase;
import ray1024.submissiontestingworker.repository.SubmissionRepository;
import ray1024.submissiontestingworker.repository.SubmissionStatusRepository;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@AllArgsConstructor
@Service
public class SubmissionService {
    private static final String PREFIX = "./submissions/";
    private static final String WORKDIR_REMOVE = "rm -rf %s";
    private static final String INPUT_FILE = "input.txt";
    private static final String OUTPUT_FILE = "output.txt";
    private static final String STREAMS_REDIRECTING = "%s>&0 &1>%s &2>/dev/null".formatted(INPUT_FILE, OUTPUT_FILE);

    private static final String CPP_SOURCE_CODE_FILENAME = "main.cpp";
    private static final String CPP_PROGRAM_FILENAME = "main";
    private static final String CPP_COMPILE = "g++ -o %s %s";
    private static final String CPP_RUN = "./%s " + STREAMS_REDIRECTING;

    private static final String JAVA_SOURCE_CODE_FILENAME = "Main.java";
    private static final String JAVA_RUN = "java %s " + STREAMS_REDIRECTING;

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

    private String workDir(Submission submission) {
        return "%s%d".formatted(PREFIX, submission.getId());
    }

    private String workDirFile(Submission submission, String filename) {
        return "%s/%s".formatted(workDir(submission), filename);
    }


    private void createWorkDirectory(Submission submission) {
        try {
            new File(workDir(submission)).mkdirs();
        } catch (Exception e) {
            changeStatus(submission, SubmissionStatus.Status.RUNTIME_ERROR);
        }
    }

    private void cleanUp(Submission submission) {
        try {
            Runtime.getRuntime().exec(new String[]{
                    WORKDIR_REMOVE.formatted(workDir(submission))
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
                    Files.writeString(Path.of(workDirFile(submission, CPP_SOURCE_CODE_FILENAME)), submission.getSourceCode());
                    Runtime.getRuntime().exec(new String[]{
                            CPP_COMPILE.formatted(workDirFile(submission, CPP_PROGRAM_FILENAME), workDirFile(submission, CPP_SOURCE_CODE_FILENAME))
                    });
                }
                case "JAVA" ->
                        Files.writeString(Path.of(workDirFile(submission, JAVA_SOURCE_CODE_FILENAME)), submission.getSourceCode());
                default -> throw new UnsupportedOperationException("Unsupported language: " + submission.getLanguage());
            }
            compileLimitsDeactivate(submission);
        } catch (Exception ignored) {
            changeStatus(submission, SubmissionStatus.Status.COMPILE_ERROR);
        }
    }

    private void testLimitsActivate(Submission submission) {

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
                            CPP_RUN.formatted(workDirFile(submission, CPP_PROGRAM_FILENAME))
                    });
                    case "JAVA" -> Runtime.getRuntime().exec(new String[]{
                            JAVA_RUN.formatted(workDirFile(submission, JAVA_SOURCE_CODE_FILENAME))
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
