package ray1024.submissiontestingworker.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ray1024.submissiontestingworker.model.entity.Submission;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> getAll(Pageable pageable);

    @Modifying
    @Query("UPDATE Submission t SET t.status = 'NEW'" +
            "WHERE t.status = 'TESTING' " +
            "AND t.lastStatusChanged >= :timeThreshold")
    void dropLongTestingSubmissions(@Param("timeThreshold") Instant timeThreshold);

    @Query("")
    Optional<Submission> reserveSubmission();
}
