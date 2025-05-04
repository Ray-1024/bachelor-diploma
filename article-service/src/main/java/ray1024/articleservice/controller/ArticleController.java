package ray1024.articleservice.controller;

import io.jsonwebtoken.Claims;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ray1024.articleservice.model.entity.Article;
import ray1024.articleservice.model.response.ArticleResponse;
import ray1024.articleservice.model.response.ArticlesResponse;
import ray1024.articleservice.service.ArticleService;
import ray1024.articleservice.service.TagService;

import java.util.List;

@RestController(value = "/api/articles")
@AllArgsConstructor
public class ArticleController {
    private ArticleService articleService;
    private TagService tagService;

    @GetMapping
    public ArticlesResponse getAll(
            @PathParam("page") @DefaultValue("1") Integer page,
            @PathParam("size") @DefaultValue("20") Integer size,
            @PathParam("tags") @DefaultValue("") List<String> tags
    ) {
        return ArticlesResponse.builder()
                .articles(articleService.getAllByTags(tagService.fromStringTags(tags), page, size))
                .build();
    }

    @GetMapping("/{id}")
    public ArticleResponse getById(@PathVariable Long id) {
        return ArticleResponse.builder()
                .article(articleService.getById(id))
                .build();
    }

    @PutMapping("/{id}")
    public ArticleResponse updateById(@PathVariable Long id,
                                      @RequestBody Article article) {
        Claims claims = (Claims) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        article.setId(id);
        article.setAuthorId(claims.get("id", Long.class));
        return ArticleResponse.builder()
                .article(articleService.update(article))
                .build();
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        Claims claims = (Claims) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        articleService.delete(id, claims.get("id", Long.class));
    }

    @PostMapping
    public ArticleResponse create(@RequestBody Article article) {
        Claims claims = (Claims) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        article.setId(null);
        article.setAuthorId(claims.get("id", Long.class));
        return ArticleResponse.builder()
                .article(articleService.create(article))
                .build();
    }
}
