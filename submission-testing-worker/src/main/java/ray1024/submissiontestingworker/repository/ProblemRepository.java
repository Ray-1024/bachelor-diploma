package ray1024.submissiontestingworker.repository;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ray1024.submissiontestingworker.model.entity.Problem;
import ray1024.submissiontestingworker.model.entity.Tag;

import java.util.List;

public interface ProblemRepository extends JpaRepository<Problem, Long> {

    Page<Problem> findAll(@NonNull Pageable pageable);

    Page<Problem> findAllByTagsContaining(List<Tag> tags, Pageable pageable);

    Page<Problem> findAllByAuthorIdAndTagsContaining(Long authorId, List<Tag> tags, Pageable pageable);
}
