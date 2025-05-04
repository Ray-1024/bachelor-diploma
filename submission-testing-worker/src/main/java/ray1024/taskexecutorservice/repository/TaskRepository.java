package ray1024.taskexecutorservice.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ray1024.taskexecutorservice.model.entity.Task;

import java.time.Instant;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> getAll(Pageable pageable);

    @Modifying
    @Query("UPDATE Task t SET t.status = 'NEW'" +
            "WHERE t.status = 'TESTING' " +
            "AND t.lastStatusChanged >= :timeThreshold")
    void dropLongTestingTasks(@Param("timeThreshold") Instant timeThreshold);

    @Query("")
    Task reserveTask();
}
