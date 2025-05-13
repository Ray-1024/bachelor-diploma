package ray1024.submissiontestingworker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ray1024.submissiontestingworker.model.entity.SubmissionStatus;

import java.util.Optional;

public interface SubmissionStatusRepository extends JpaRepository<SubmissionStatus, Long> {

    Optional<SubmissionStatus> findByStatus(String status);
}
