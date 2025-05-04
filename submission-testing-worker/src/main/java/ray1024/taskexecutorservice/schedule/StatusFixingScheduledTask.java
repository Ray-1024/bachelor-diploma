package ray1024.taskexecutorservice.schedule;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ray1024.taskexecutorservice.service.TaskService;

@Component
@AllArgsConstructor
public class StatusFixingScheduledTask {
    private final TaskService taskService;

    @Scheduled(fixedDelay = 60000)
    public void fixWrongStatuses() {
        taskService.dropLongTestingTasks();
    }
}
