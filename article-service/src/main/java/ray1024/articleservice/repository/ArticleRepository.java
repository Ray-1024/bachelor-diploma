package ray1024.articleservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ray1024.articleservice.model.entity.Article;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Page<Article> findAll(Pageable pageable);

    Page<Article> findAllByTitleOrArticleContainingIgnoreCaseAndTagsContaining(String title, String article, List<Long> tags, Pageable pageable);

    Page<Article> findAllByAuthorIdAndTitleOrArticleContainingIgnoreCaseAndTagsContaining(Long authorId, String title, String article, List<Long> tags, Pageable pageable);
}
