package ray1024.problemservice.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ray1024.problemservice.model.dto.TestCaseDto;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UpdateProblemRequest {
    private String title;
    private String description;
    private String input;
    private String output;
    private Long memoryLimitBytes;
    private Long timeLimitMilliseconds;
    private List<TestCaseDto> samples;
    private List<TestCaseDto> tests;
    private List<Long> tags;
}
