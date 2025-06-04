package ray1024.articleservice.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ray1024.articleservice.exception.ArticleNotFoundException;
import ray1024.articleservice.exception.WrongAuthorException;
import ray1024.articleservice.model.entity.Article;
import ray1024.articleservice.model.request.UpdateArticleRequest;
import ray1024.articleservice.repository.ArticleRepository;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Service
public class ArticleService {
    private ArticleRepository articleRepository;

    public List<Article> getAllByTags(String search, List<Long> tags, int page, int size) {
        return articleRepository.findAllByTitleOrArticleContainingIgnoreCaseAndTagsContaining(
                search,
                search,
                tags,
                PageRequest.of(
                        page,
                        size,
                        Sort.Direction.DESC,
                        "creationDate"
                )
        ).getContent();
    }

    public List<Article> getAllByTagsAndAuthor(Long authorId, String search, List<Long> tags, int page, int size) {
        return articleRepository.findAllByAuthorIdAndTitleOrArticleContainingIgnoreCaseAndTagsContaining(
                authorId,
                search,
                search,
                tags,
                PageRequest.of(
                        page,
                        size,
                        Sort.Direction.DESC,
                        "creationDate"
                )
        ).getContent();
    }


    public Article getById(long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new ArticleNotFoundException(String.valueOf(id)));
    }

    public Article create(long userId, @NonNull String authorUsername, @NonNull UpdateArticleRequest request) {
        return articleRepository.save(Article.builder()
                .id(null)
                .authorId(userId)
                .authorUsername(authorUsername)
                .creationDate(Instant.now())
                .title(request.getTitle())
                .article(request.getArticle())
                .tags(request.getTags())
                .build());
    }

    public Article update(long articleId, long userId, @NonNull UpdateArticleRequest request) {
        Article old = articleRepository.findById(articleId).orElseThrow(() -> new ArticleNotFoundException(String.valueOf(articleId)));
        if (!Objects.equals(old.getAuthorId(), userId)) {
            throw new WrongAuthorException(String.valueOf(userId));
        }
        old.setTitle(request.getTitle());
        old.setArticle(request.getArticle());
        old.setTags(request.getTags());
        return articleRepository.save(old);
    }

    public void delete(long userId, long id) {
        Article old = articleRepository.findById(id).orElseThrow(() -> new ArticleNotFoundException(String.valueOf(id)));
        if (!Objects.equals(old.getAuthorId(), userId)) {
            throw new WrongAuthorException(String.valueOf(userId));
        }
        articleRepository.deleteById(id);
    }
}
