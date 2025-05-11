package ray1024.problemservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ray1024.problemservice.model.response.TagListResponse;
import ray1024.problemservice.model.response.TagResponse;
import ray1024.problemservice.service.TagService;

import java.util.Objects;

@RestController(value = "/api/tags")
@AllArgsConstructor
public class TagController {
    private final TagService tagService;

    @GetMapping
    public TagListResponse getAll(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer size
    ) {
        if (Objects.isNull(page)) page = 1;
        if (Objects.isNull(size)) size = 5;
        return TagListResponse.builder()
                .tags(tagService.getAll(page, size))
                .page(page)
                .size(size)
                .build();
    }

    @GetMapping("/{tagId}")
    public TagResponse getById(@PathVariable Long tagId) {
        return TagResponse.builder()
                .tag(tagService.getById(tagId))
                .build();
    }
}
