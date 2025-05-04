package ray1024.problemservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ray1024.problemservice.model.request.TagNameRequest;
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

    @GetMapping("/{id}")
    public TagResponse getById(@PathVariable Long id) {
        return TagResponse.builder()
                .tag(tagService.getById(id))
                .build();
    }

    @PutMapping("/{id}")
    public TagResponse updateById(@PathVariable Long id, @RequestBody TagNameRequest request) {
        return TagResponse.builder()
                .tag(tagService.updateById(id, request.getName()))
                .build();
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        tagService.deleteById(id);
    }

    @PostMapping
    public TagResponse create(@RequestBody TagNameRequest request) {
        return TagResponse.builder()
                .tag(tagService.create(request.getName()))
                .build();
    }
}
