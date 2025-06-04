package ray1024.submissiontestingworker.schedule;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ray1024.submissiontestingworker.service.SubmissionService;

@Component
@AllArgsConstructor
public class FlushLongTestingSubmissionsTask {
    private final SubmissionService taskService;

    @Scheduled(fixedDelay = 20000)
    public void flushLongTestingSubmissions() {
        taskService.flushLongTestingSubmissions();
    }
}
