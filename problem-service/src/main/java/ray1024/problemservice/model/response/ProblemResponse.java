package ray1024.problemservice.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ray1024.problemservice.model.entity.Problem;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProblemResponse {
    private Problem problem;
}
