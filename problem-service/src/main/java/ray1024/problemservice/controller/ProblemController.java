package ray1024.problemservice.controller;

import io.jsonwebtoken.Claims;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ray1024.problemservice.model.request.UpdateProblemRequest;
import ray1024.problemservice.model.response.ProblemResponse;
import ray1024.problemservice.model.response.ProblemsResponse;
import ray1024.problemservice.service.ProblemService;

import java.util.List;
import java.util.Objects;

@RestController(value = "/api/problems")
@AllArgsConstructor
public class ProblemController {
    private final ProblemService problemService;

    @GetMapping
    public ProblemsResponse getAll(
            @PathParam("page") @DefaultValue("1") Integer page,
            @PathParam("size") @DefaultValue("20") Integer size,
            @PathParam("search") @DefaultValue("") String search,
            @PathParam("tags") @DefaultValue("") List<Long> tags
    ) {
        if (Objects.isNull(page)) page = 1;
        if (Objects.isNull(size)) size = 5;
        return ProblemsResponse.builder()
                .problems(problemService.getAll(search, tags, page, size))
                .page(page)
                .size(size)
                .build();
    }

    @GetMapping("/author")
    public ProblemsResponse getAllByAuthorId(
            @PathParam("page") @DefaultValue("1") Integer page,
            @PathParam("size") @DefaultValue("20") Integer size,
            @PathParam("search") @DefaultValue("") String search,
            @PathParam("tags") @DefaultValue("") List<Long> tags
    ) {
        if (Objects.isNull(page)) page = 1;
        if (Objects.isNull(size)) size = 5;
        Claims claims = (Claims) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ProblemsResponse.builder()
                .problems(problemService.getAllByAuthorId(
                        claims.get("id", Long.class),
                        search, tags, page, size))
                .page(page)
                .size(size)
                .build();
    }

    @GetMapping("/{problemId}")
    public ProblemResponse getById(@PathVariable Long problemId) {
        return ProblemResponse.builder()
                .problem(problemService.getById(problemId))
                .build();
    }

    @PutMapping("/{problemId}")
    public ProblemResponse updateById(@PathVariable Long problemId, @RequestBody UpdateProblemRequest request) {
        Claims claims = (Claims) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ProblemResponse.builder()
                .problem(problemService.updateById(problemId,
                        claims.get("id", Long.class), request))
                .build();
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        Claims claims = (Claims) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        problemService.deleteById(id, claims.get("id", Long.class));
    }

    @PostMapping
    public ProblemResponse create(@RequestBody UpdateProblemRequest request) {
        Claims claims = (Claims) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long id = claims.get("id", Long.class);
        return ProblemResponse.builder()
                .problem(problemService.create(id, request))
                .build();
    }
}
