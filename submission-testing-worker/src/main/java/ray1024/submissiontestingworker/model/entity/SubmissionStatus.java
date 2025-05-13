package ray1024.submissiontestingworker.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "submissionstatuses")
public class SubmissionStatus {

    public enum Status {
        NEW, TESTING, OK, RUNTIME_ERROR, WRONG_ANSWER, COMPILE_ERROR
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String status;
}
