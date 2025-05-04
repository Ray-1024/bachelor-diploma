package ray1024.taskexecutorservice.model.entity;

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
@Table(name = "problemtests")
public class ProblemTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
