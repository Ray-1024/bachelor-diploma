package ray1024.submissiontestingworker.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.Optional;

@AllArgsConstructor
@Service
public class SubmissionService {
    private static final String WORKDIR = "/submissions/%s/";
    private static final String WORKDIR_FILE = WORKDIR + "%s";
    private static final String REMOVE_COMMAND = "rm -rf %s";
    private static final String WORKDIR_REMOVE_COMMAND = "rm -rf " + WORKDIR;

    private static final String APPARMOR_PROFILE_FILE = "/etc/apparmor.d/%s";
    private static final String APPARMOR_ACTIVATE_PROFILE_COMMAND = "aa-enforce %s";
    private static final String APPARMOR_DEACTIVATE_PROFILE_COMMAND = "aa-disable %s";

    private static final String INPUT_FILE = "input.txt";
    private static final String OUTPUT_FILE = "output.txt";
    private static final String STREAMS_REDIRECTING = "<%s >%s 2>/dev/null".formatted(INPUT_FILE, OUTPUT_FILE);

    private static final String CPP_SOURCE_CODE_FILE = "main.cpp";
    private static final String CPP_PROGRAM_FILE = "main";
    private static final String CPP_COMPILE_COMMAND = "g++ -o %s %s";
    private static final String CPP_RUN_COMMAND = "./%s " + STREAMS_REDIRECTING;

    private static final String JAVA_SOURCE_CODE_FILE = "Main.java";
    private static final String JAVA_RUN_COMMAND = "java %s " + STREAMS_REDIRECTING;

    private static final String ULIMIT_COMMAND = "ulimit -t %s -v %s";

    private final SubmissionRepository submissionRepository;
    private final SubmissionStatusRepository submissionStatusRepository;

    public void flushLongTestingSubmissions() {
        submissionRepository.flushLongTestingSubmissions(Instant.now().minus(30, ChronoUnit.SECONDS));
    }

    protected void changeStatus(Submission submission, SubmissionStatus.Status statusEnum) {
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

    private void compile(Submission submission) {
        try {
            switch (submission.getLanguage().getLanguage()) {
                case "CPP" -> {
                    Files.writeString(Path.of(WORKDIR_FILE.formatted(submission.getId().toString(), CPP_SOURCE_CODE_FILE)), submission.getSourceCode());
                    Runtime.getRuntime().exec(new String[]{
                            CPP_COMPILE_COMMAND.formatted(WORKDIR_FILE.formatted(submission.getId().toString(), CPP_PROGRAM_FILE), WORKDIR_FILE.formatted(submission.getId().toString(), CPP_SOURCE_CODE_FILE))
                    });
                }
                case "JAVA" ->
                        Files.writeString(Path.of(WORKDIR_FILE.formatted(submission.getId().toString(), JAVA_SOURCE_CODE_FILE)), submission.getSourceCode());
                default -> throw new UnsupportedOperationException("Unsupported language: " + submission.getLanguage());
            }
        } catch (Exception ignored) {
            changeStatus(submission, SubmissionStatus.Status.COMPILE_ERROR);
        }
    }

    private String cppAppArmorConfig(Submission submission) {
        return new StringBuilder()
                .append("abi <abi/3.0>,").append('\n')
                .append("include <tunables/global>").append('\n')
                .append(WORKDIR_FILE.formatted(submission.getId().toString(), CPP_PROGRAM_FILE)).append(" {\n")
                .append("\tinclude <abstractions/base>").append('\n')
                .append("\tinclude <abstractions/bash>").append('\n')
                .append('\t').append(WORKDIR_FILE.formatted(submission.getId().toString(), CPP_PROGRAM_FILE)).append(" ix,\n")
                .append('\t').append(WORKDIR_FILE.formatted(submission.getId().toString(), INPUT_FILE)).append(" r,\n")
                .append('\t').append(WORKDIR_FILE.formatted(submission.getId().toString(), OUTPUT_FILE)).append(" w,\n")
                .append("\t}\n")
                .toString();
    }

    private String javaAppArmorConfig(Submission submission) {
        return new StringBuilder()
                .append("abi <abi/3.0>,").append('\n')
                .append("include <tunables/global>").append('\n')
                .append(WORKDIR_FILE.formatted(submission.getId().toString(), JAVA_SOURCE_CODE_FILE)).append(" {\n")
                .append("\tinclude <abstractions/base>").append('\n')
                .append("\tinclude <abstractions/bash>").append('\n')
                .append('\t').append(WORKDIR_FILE.formatted(submission.getId().toString(), JAVA_SOURCE_CODE_FILE)).append(" ix,\n")
                .append('\t').append(WORKDIR_FILE.formatted(submission.getId().toString(), INPUT_FILE)).append(" r,\n")
                .append('\t').append(WORKDIR_FILE.formatted(submission.getId().toString(), OUTPUT_FILE)).append(" w,\n")
                .append("\t}\n")
                .toString();
    }

    private void testLimitsActivate(Submission submission) throws IOException {
        switch (submission.getLanguage().getLanguage()) {
            case "JAVA" -> {
                String filename = WORKDIR_FILE.formatted(submission.getId().toString(), JAVA_SOURCE_CODE_FILE);
                String profileName = filename.replace('/', '.');
                Files.writeString(Path.of(APPARMOR_PROFILE_FILE.formatted(profileName)), javaAppArmorConfig(submission));
                Runtime.getRuntime().exec(new String[]{
                        APPARMOR_ACTIVATE_PROFILE_COMMAND.formatted(filename),
                });
            }
            case "CPP" -> {
                String filename = WORKDIR_FILE.formatted(submission.getId().toString(), CPP_PROGRAM_FILE);
                String profileName = filename.replace('/', '.');
                Files.writeString(Path.of(APPARMOR_PROFILE_FILE.formatted(profileName)), cppAppArmorConfig(submission));
                Runtime.getRuntime().exec(new String[]{
                        APPARMOR_ACTIVATE_PROFILE_COMMAND.formatted(filename),
                });
            }
            default -> throw new UnsupportedOperationException("Unsupported language: " + submission.getLanguage());
        }
    }

    private void testLimitsDeactivate(Submission submission) throws IOException {
        switch (submission.getLanguage().getLanguage()) {
            case "JAVA" -> {
                String filename = WORKDIR_FILE.formatted(submission.getId().toString(), JAVA_SOURCE_CODE_FILE);
                String profileName = filename.replace('/', '.');
                Runtime.getRuntime().exec(new String[]{
                        APPARMOR_DEACTIVATE_PROFILE_COMMAND.formatted(filename),
                        REMOVE_COMMAND.formatted(APPARMOR_PROFILE_FILE.formatted(profileName))
                });
            }
            case "CPP" -> {
                String filename = WORKDIR_FILE.formatted(submission.getId().toString(), CPP_PROGRAM_FILE);
                String profileName = filename.replace('/', '.');
                Runtime.getRuntime().exec(new String[]{
                        APPARMOR_DEACTIVATE_PROFILE_COMMAND.formatted(filename),
                        REMOVE_COMMAND.formatted(APPARMOR_PROFILE_FILE.formatted(profileName))
                });
            }
            default -> throw new UnsupportedOperationException("Unsupported language: " + submission.getLanguage());
        }
    }

    private String limitsCommand(Submission submission) {
        return ULIMIT_COMMAND.formatted(
                String.valueOf((submission.getProblem().getTimeLimitMilliseconds() + 999) / 1000),
                String.valueOf((submission.getProblem().getMemoryLimitBytes() + 1023) / 1024)
        );
    }

    private void test(Submission submission) {
        try {
            testLimitsActivate(submission);
            SubmissionStatus.Status status = null;
            for (TestCase aCase : submission.getProblem().getTests()) {
                Files.writeString(Path.of(WORKDIR_FILE.formatted(submission.getId().toString(), INPUT_FILE)), aCase.getInput());

                switch (submission.getLanguage().getLanguage()) {
                    case "CPP" -> Runtime.getRuntime().exec(new String[]{
                            limitsCommand(submission),
                            CPP_RUN_COMMAND.formatted(WORKDIR_FILE.formatted(submission.getId().toString(), CPP_PROGRAM_FILE))
                    });
                    case "JAVA" -> Runtime.getRuntime().exec(new String[]{
                            limitsCommand(submission),
                            JAVA_RUN_COMMAND.formatted(WORKDIR_FILE.formatted(submission.getId().toString(), JAVA_SOURCE_CODE_FILE))
                    });
                    default ->
                            throw new UnsupportedOperationException("Unsupported language: " + submission.getLanguage());
                }
                String outputTxt;
                outputTxt = Files.readString(Path.of(WORKDIR_FILE.formatted(submission.getId().toString(), OUTPUT_FILE)));
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

    @Transactional
    protected Optional<Submission> reserveSubmission() {
        Optional<Submission> submission = submissionRepository.findSubmissionByStatusStatus(SubmissionStatus.Status.NEW.name());
        if (submission.isEmpty()) return Optional.empty();
        SubmissionStatus status = submissionStatusRepository.findByStatus(SubmissionStatus.Status.TESTING.name()).orElseThrow();
        submission.get().setStatus(status);
        return Optional.of(submissionRepository.save(submission.get()));
    }

    @Transactional
    public void reserveAndTestSubmission() {
        reserveSubmission().ifPresent(submission -> {
            cleanUp(submission);
            createWorkDirectory(submission);
            compile(submission);
            test(submission);
            cleanUp(submission);
        });
    }
}
