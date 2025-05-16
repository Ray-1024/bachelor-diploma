package ray1024.tagservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ray1024.tagservice.model.entity.Tag;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByNameIgnoreCase(String name);
}
