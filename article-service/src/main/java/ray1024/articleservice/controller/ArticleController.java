package ray1024.articleservice.controller;

import io.jsonwebtoken.Claims;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ray1024.articleservice.model.request.CreateArticleRequest;
import ray1024.articleservice.model.request.UpdateArticleRequest;
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

    @GetMapping("/author")
    public ArticlesResponse getAllByUser(
            @PathParam("page") @DefaultValue("1") Integer page,
            @PathParam("size") @DefaultValue("20") Integer size,
            @PathParam("tags") @DefaultValue("") List<String> tags
    ) {
        Claims claims = (Claims) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ArticlesResponse.builder()
                .articles(articleService.getAllByTagsAndAuthor(
                        claims.get("id", Long.class), tagService.fromStringTags(tags), page, size))
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
                                      @RequestBody UpdateArticleRequest request) {
        Claims claims = (Claims) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        request.setId(id);
        return ArticleResponse.builder()
                .article(articleService.update(claims.get("id", Long.class), request))
                .build();
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        Claims claims = (Claims) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        articleService.delete(claims.get("id", Long.class), id);
    }

    @PostMapping
    public ArticleResponse create(@RequestBody CreateArticleRequest request) {
        Claims claims = (Claims) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ArticleResponse.builder()
                .article(articleService.create(claims.get("id", Long.class), request))
                .build();
    }
}
