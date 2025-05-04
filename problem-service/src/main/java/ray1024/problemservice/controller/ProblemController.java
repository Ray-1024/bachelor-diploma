package ray1024.problemservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ray1024.problemservice.client.UserServiceClient;
import ray1024.problemservice.model.request.ProblemRequest;
import ray1024.problemservice.model.response.ProblemListResponse;
import ray1024.problemservice.model.response.ProblemResponse;
import ray1024.problemservice.service.ProblemService;

import java.util.Objects;

@RestController(value = "/api/problems")
@AllArgsConstructor
public class ProblemController {
    private final ProblemService problemService;
    private final UserServiceClient userServiceClient;

    @GetMapping
    public ProblemListResponse getAll(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer size
    ) {
        if (Objects.isNull(page)) page = 1;
        if (Objects.isNull(size)) size = 5;
        return ProblemListResponse.builder()
                .problems(problemService.getAll(page, size))
                .page(page)
                .size(size)
                .build();
    }

    @GetMapping("/{id}")
    public ProblemResponse getById(@PathVariable Long id) {
        return ProblemResponse.builder()
                .problem(problemService.getById(id))
                .build();
    }

    @PutMapping("/{id}")
    public ProblemResponse updateById(@PathVariable Long id, @RequestBody ProblemRequest request) {
        return ProblemResponse.builder()
                .problem(problemService.updateById(id, request.getProblem()))
                .build();
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        problemService.deleteById(id);
    }

    @PostMapping
    public ProblemResponse create(@RequestBody ProblemRequest request,
                                  @RequestHeader(name = "Authorization") String authorization) {
        String token = (authorization == null || !authorization.startsWith("Bearer ")) ? "" :
                authorization.substring(7);
        return ProblemResponse.builder()
                .problem(problemService.create(userServiceClient.getUserId(token), request.getProblem()))
                .build();
    }
}
