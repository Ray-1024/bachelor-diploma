package ray1024.problemservice.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ray1024.problemservice.client.UserServiceClient;
import ray1024.problemservice.exception.ProblemNotFoundException;
import ray1024.problemservice.model.entity.Problem;
import ray1024.problemservice.repository.ProblemRepository;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@Service
public class ProblemService {
    private final ProblemRepository problemRepository;

    public List<Problem> getAll(int page, int size) {
        return problemRepository.findAll(Pageable.ofSize(size).withPage(page)).getContent();
    }

    public Problem getById(long id) {
        return problemRepository.findById(id).orElseThrow(() -> new ProblemNotFoundException("Problem not found"));
    }

    @Transactional
    public Problem updateById(long id, @NonNull Problem problem) {
        Problem oldProblem = problemRepository.findById(id).orElseThrow(() -> new ProblemNotFoundException("Problem not found"));
        oldProblem.setTitle(problem.getTitle());
        oldProblem.setDescription(problem.getDescription());
        oldProblem.setInput(problem.getInput());
        oldProblem.setOutput(problem.getOutput());
        oldProblem.setTags(problem.getTags());
        oldProblem.setTests(problem.getTests());
        return problemRepository.save(oldProblem);
    }

    public void deleteById(long id) {
        problemRepository.deleteById(id);
    }

    @Transactional
    public Problem create(long userId, @NonNull Problem problem) {
        problem.setId(null);
        problem.setAuthorId(userId);
        problem.setCreationDate(Instant.now());
        return problemRepository.save(problem);
    }
}
