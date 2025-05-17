package ray1024.problemservice.controller;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ray1024.problemservice.model.entity.Submission;
import ray1024.problemservice.model.request.CreateSubmissionRequest;
import ray1024.problemservice.model.response.SubmissionResponse;
import ray1024.problemservice.model.response.SubmissionsResponse;
import ray1024.problemservice.service.SubmissionService;

import java.util.Objects;

@RestController
@AllArgsConstructor
public class SubmissionController {
    private final SubmissionService submissionService;

    private SubmissionResponse fromEntity(Submission submission) {
        return SubmissionResponse.builder()
                .status(submission.getStatus().getStatus())
                .creationTime(submission.getCreationTime())
                .sourceCode(submission.getSourceCode())
                .language(submission.getLanguage().getLanguage())
                .build();
    }

    @GetMapping("/api/problems/{problemId}/submissions")
    public SubmissionsResponse getAll(
            @PathVariable Long problemId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer size
    ) {
        if (Objects.isNull(page)) page = 1;
        if (Objects.isNull(size)) size = 5;
        Claims claims = (Claims) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return SubmissionsResponse.builder()
                .submissions(submissionService.getAllByProblemIdAndAuthorId(problemId, claims.get("id", Long.class), page, size).stream()
                        .map(this::fromEntity).toList())
                .page(page)
                .size(size)
                .build();
    }

    @PostMapping("/api/problems/{problemId}/submissions")
    public SubmissionResponse create(
            @PathVariable Long problemId,
            @RequestBody CreateSubmissionRequest request) {
        Claims claims = (Claims) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long authorId = claims.get("id", Long.class);
        return fromEntity(submissionService.create(problemId, authorId, request));
    }

    @GetMapping("/api/submissions/author")
    public SubmissionsResponse getAllByAuthorId(@RequestParam(defaultValue = "1") Integer page,
                                                @RequestParam(defaultValue = "5") Integer size) {
        if (Objects.isNull(page)) page = 1;
        if (Objects.isNull(size)) size = 5;
        Claims claims = (Claims) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return SubmissionsResponse.builder()
                .submissions(submissionService.getAllByAuthorId(claims.get("id", Long.class), page, size).stream()
                        .map(this::fromEntity).toList())
                .page(page)
                .size(size)
                .build();
    }
}
