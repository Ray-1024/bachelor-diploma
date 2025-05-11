package ray1024.problemservice.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ray1024.problemservice.model.entity.Submission;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SubmissionsResponse {
    private List<SubmissionResponse> submissions;
    private Integer page;
    private Integer size;
}
