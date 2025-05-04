package ray1024.articleservice.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ray1024.articleservice.exception.ArticleNotFoundException;
import ray1024.articleservice.exception.WrongAuthorException;
import ray1024.articleservice.model.entity.Article;
import ray1024.articleservice.model.entity.Tag;
import ray1024.articleservice.repository.ArticleRepository;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Service
public class ArticleService {
    private ArticleRepository articleRepository;

    public List<Article> getAllByTags(List<Tag> tags, int page, int size) {
        return articleRepository.findAllByTags(
                tags,
                tags.size(),
                Pageable.ofSize(size).withPage(page)
        ).getContent();
    }

    public Article getById(long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new ArticleNotFoundException(String.valueOf(id)));
    }

    public Article create(@NonNull Article article) {
        return articleRepository.save(article);
    }

    public Article update(@NonNull Article article) {
        Article old = articleRepository.findById(article.getId()).orElseThrow(() -> new ArticleNotFoundException(String.valueOf(article.getId())));
        if (!Objects.equals(old.getAuthorId(), article.getAuthorId())) {
            throw new WrongAuthorException(String.valueOf(article.getAuthorId()));
        }
        return articleRepository.save(article);
    }

    public void delete(long id, long userId) {
        Article old = articleRepository.findById(id).orElseThrow(() -> new ArticleNotFoundException(String.valueOf(id)));
        if (!Objects.equals(old.getAuthorId(), userId)) {
            throw new WrongAuthorException(String.valueOf(userId));
        }
        articleRepository.deleteById(id);
    }
}
