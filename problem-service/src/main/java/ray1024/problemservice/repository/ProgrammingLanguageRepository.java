package ray1024.problemservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ray1024.problemservice.model.entity.ProgrammingLanguage;

import java.util.Optional;

public interface ProgrammingLanguageRepository extends JpaRepository<ProgrammingLanguage, Long> {

    Optional<ProgrammingLanguage> findByLanguage(String language);
}
