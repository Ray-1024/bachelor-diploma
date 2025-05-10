package ray1024.articleservice.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ray1024.articleservice.exception.ArticleNotFoundException;
import ray1024.articleservice.exception.WrongAuthorException;
import ray1024.articleservice.model.entity.Article;
import ray1024.articleservice.model.entity.Tag;
import ray1024.articleservice.model.request.CreateArticleRequest;
import ray1024.articleservice.model.request.UpdateArticleRequest;
import ray1024.articleservice.repository.ArticleRepository;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Service
public class ArticleService {
    private ArticleRepository articleRepository;
    private TagService tagService;

    public List<Article> getAllByTags(List<Tag> tags, int page, int size) {
        return articleRepository.findAllByTagsContaining(
                tags,
                PageRequest.of(
                        page,
                        size,
                        Sort.Direction.DESC,
                        "creationDate"
                )
        ).getContent();
    }

    public List<Article> getAllByTagsAndAuthor(Long authorId, List<Tag> tags, int page, int size) {
        return articleRepository.findAllByAuthorIdAndTagsContaining(
                authorId,
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

    public Article create(long userId, @NonNull CreateArticleRequest request) {
        return articleRepository.save(Article.builder()
                .id(null)
                .authorId(userId)
                .creationDate(Instant.now())
                .title(request.getTitle())
                .article(request.getArticle())
                .tags(tagService.fromStringTags(request.getTags()))
                .build());
    }

    public Article update(long userId, @NonNull UpdateArticleRequest request) {
        Article old = articleRepository.findById(request.getId()).orElseThrow(() -> new ArticleNotFoundException(String.valueOf(request.getId())));
        if (!Objects.equals(old.getAuthorId(), userId)) {
            throw new WrongAuthorException(String.valueOf(userId));
        }
        old.setTitle(request.getTitle());
        old.setArticle(request.getArticle());
        old.setTags(tagService.fromStringTags(request.getTags()));
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
