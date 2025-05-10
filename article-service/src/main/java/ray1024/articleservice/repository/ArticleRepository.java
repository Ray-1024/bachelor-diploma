package ray1024.articleservice.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ray1024.articleservice.model.entity.Article;
import ray1024.articleservice.model.entity.Tag;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    @NotNull Page<Article> findAll(@NotNull Pageable pageable);

    Page<Article> findAllByTagsContaining(List<Tag> tags, Pageable pageable);

    Page<Article> findAllByAuthorIdAndTagsContaining(Long authorId, List<Tag> tags, Pageable pageable);

    Page<Article> findAllByArticleContainingIgnoreCaseOrTitleContainingIgnoreCase(String content,
                                                                                  Pageable pageable);
}
