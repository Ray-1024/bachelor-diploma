package ray1024.problemservice.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ray1024.problemservice.exception.ProblemNotFoundException;
import ray1024.problemservice.model.entity.Problem;
import ray1024.problemservice.model.entity.TestCase;
import ray1024.problemservice.model.request.UpdateProblemRequest;
import ray1024.problemservice.repository.ProblemRepository;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@Service
public class ProblemService {
    private final ProblemRepository problemRepository;

    public List<Problem> getAll(String text, List<Long> tags, int page, int size) {
        return problemRepository.findAllByTagsContaining(text, text, tags, Pageable.ofSize(size).withPage(page)).getContent();
    }

    public List<Problem> getAllByAuthorId(long authorId, String text, List<Long> tags, int page, int size) {
        return problemRepository.findAllByAuthorIdAndTagsContaining(authorId, text, text, tags, Pageable.ofSize(size).withPage(page)).getContent();
    }


    public Problem getById(long id) {
        return problemRepository.findById(id).orElseThrow(() -> new ProblemNotFoundException("Problem not found"));
    }

    @Transactional
    public Problem updateById(long problemId, long authorId, @NonNull UpdateProblemRequest request) {
        Problem oldProblem = problemRepository.findById(problemId).orElseThrow(() -> new ProblemNotFoundException("Problem not found"));

        return problemRepository.save(oldProblem);
    }

    public void deleteById(long problemId, long authorId) {
        Problem problem = problemRepository.findById(problemId).orElseThrow(() -> new ProblemNotFoundException("Problem not found"));
        if (problem.getAuthorId() != authorId)
            throw new ProblemNotFoundException("Problem not found");
        problemRepository.deleteById(problemId);
    }

    @Transactional
    public Problem create(long authorId, @NonNull UpdateProblemRequest request) {
        return problemRepository.save(Problem.builder()
                .id(null)
                .input(request.getInput())
                .authorId(authorId)
                .title(request.getTitle())
                .description(request.getDescription())
                .memoryLimitBytes(request.getMemoryLimitBytes())
                .creationDate(Instant.now())
                .timeLimitMilliseconds(request.getTimeLimitMilliseconds())
                .tags(request.getTags())
                .samples(request.getSamples().stream().map(testCaseDto ->
                        TestCase.builder()
                                .id(null)
                                .input(testCaseDto.getInput())
                                .output(testCaseDto.getOutput())
                                .build()).toList())
                .tests(request.getTests().stream().map(testCaseDto ->
                        TestCase.builder()
                                .id(null)
                                .input(testCaseDto.getInput())
                                .output(testCaseDto.getOutput())
                                .build()).toList())
                .build());
    }
}
