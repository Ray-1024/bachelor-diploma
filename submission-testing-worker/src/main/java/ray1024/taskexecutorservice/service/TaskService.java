package ray1024.taskexecutorservice.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ray1024.taskexecutorservice.model.entity.Task;
import ray1024.taskexecutorservice.repository.TaskRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@AllArgsConstructor
@Service
public class TaskService {
    private final TaskRepository taskRepository;

    @Transactional
    public void dropLongTestingTasks() {
        taskRepository.dropLongTestingTasks(Instant.now().minus(30, ChronoUnit.SECONDS));
    }

    @Transactional
    public Task reserveTask() {
        return null;
    }

    public void testTask(@NonNull Task task) {

    }
}
