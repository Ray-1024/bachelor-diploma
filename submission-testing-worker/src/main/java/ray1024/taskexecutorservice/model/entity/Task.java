package ray1024.taskexecutorservice.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false)
    private TaskStatus status;

    @Column(nullable = false)
    private Instant lastStatusChanged;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Problem problem;

    @Column(nullable = false)
    private Instant creationTime;

    @ManyToOne
    @JoinColumn(nullable = false)
    private ProgrammingLanguage language;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String sourceCode;
}
