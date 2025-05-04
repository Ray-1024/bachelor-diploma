package ray1024.problemservice.repository;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ray1024.problemservice.model.entity.Tag;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Page<Tag> findAll(@NonNull Pageable pageable);

    Optional<Tag> findByName(@NonNull String name);
}
