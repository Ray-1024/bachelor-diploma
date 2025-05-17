package ray1024.problemservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ray1024.problemservice.model.entity.Submission;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    Page<Submission> findAllByAuthorIdAndProblemId(long authorId, long problemId, Pageable pageable);

    Page<Submission> findAllByAuthorId(long authorId, Pageable pageable);
}
