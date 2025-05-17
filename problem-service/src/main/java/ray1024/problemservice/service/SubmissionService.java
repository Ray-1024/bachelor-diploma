package ray1024.problemservice.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ray1024.problemservice.exception.ProblemNotFoundException;
import ray1024.problemservice.model.entity.Submission;
import ray1024.problemservice.model.entity.SubmissionStatus;
import ray1024.problemservice.model.request.CreateSubmissionRequest;
import ray1024.problemservice.repository.ProblemRepository;
import ray1024.problemservice.repository.ProgrammingLanguageRepository;
import ray1024.problemservice.repository.SubmissionRepository;
import ray1024.problemservice.repository.SubmissionStatusRepository;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@Service
public class SubmissionService {
    private final SubmissionRepository submissionRepository;
    private final ProblemRepository problemRepository;
    private final SubmissionStatusRepository submissionStatusRepository;
    private final ProgrammingLanguageRepository programmingLanguageRepository;

    public List<Submission> getAllByProblemIdAndAuthorId(long problemId, long authorId, int page, int size) {
        return submissionRepository.findAllByAuthorIdAndProblemId(authorId, problemId, Pageable.ofSize(size).withPage(page)).getContent();
    }

    public List<Submission> getAllByAuthorId(long authorId, int page, int size) {
        return submissionRepository.findAllByAuthorId(authorId, Pageable.ofSize(size).withPage(page)).getContent();
    }


    @Transactional
    public Submission create(long problemId, long authorId, @NonNull CreateSubmissionRequest request) {
        Instant now = Instant.now();
        return submissionRepository.save(Submission.builder()
                .id(null)
                .authorId(authorId)
                .status(submissionStatusRepository.findByStatus(SubmissionStatus.Status.NEW.name()).orElseThrow(() -> new RuntimeException("Submission status not found")))
                .lastStatusChanged(now)
                .problem(problemRepository.findById(problemId).orElseThrow(() -> new ProblemNotFoundException("Problem not found")))
                .creationTime(now)
                .language(programmingLanguageRepository.findByLanguage(request.getLanguage()).orElseThrow(() -> new RuntimeException("Language not found")))
                .sourceCode(request.getSourceCode())
                .build());
    }
}
