package ray1024.taskexecutorservice.schedule;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ray1024.taskexecutorservice.service.TaskService;

@AllArgsConstructor
@Component
public class TestingScheduledTask {
    private final TaskService taskService;

    @Scheduled(fixedDelay = 1000)
    public void testing() {

    }
}
