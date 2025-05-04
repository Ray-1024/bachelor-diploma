package ray1024.problemservice.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ray1024.problemservice.model.entity.Problem;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProblemRequest {
    private Problem problem;
}
