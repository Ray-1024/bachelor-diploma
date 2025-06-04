package ray1024.submissiontestingworker.schedule;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ray1024.submissiontestingworker.service.SubmissionService;

@AllArgsConstructor
@Component
public class TestingSubmissionsTask {
    private final SubmissionService taskService;

    @Scheduled(fixedDelay = 3000)
    public void testingSubmissions() {
        taskService.reserveAndTestSubmission();
    }
}
