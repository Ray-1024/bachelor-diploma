package ray1024.articleservice.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ray1024.articleservice.model.entity.Article;
import ray1024.articleservice.model.entity.Tag;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    @NotNull Page<Article> findAll(@NotNull Pageable pageable);

    @Query("SELECT a FROM Article a JOIN a.tags t WHERE t IN :tags GROUP BY a HAVING COUNT(t) >= :tagCount")
    Page<Article> findAllByTags(@Param("tags") List<Tag> tags,
                                @Param("tagCount") long tagCount,
                                Pageable pageable);
}
