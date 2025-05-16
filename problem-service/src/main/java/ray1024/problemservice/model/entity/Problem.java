package ray1024.problemservice.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "problems")
public class Problem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @Column(nullable = false)
    private Long authorId;

    @JsonIgnore
    @Column(nullable = false)
    private Instant creationDate;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String input;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String output;

    @Column(nullable = false)
    @Min(value = 0)
    @Max(value = 536870912)
    private Long memoryLimitBytes;

    @Column(nullable = false)
    @Min(value = 0)
    @Max(value = 10000)
    private Long timeLimitMilliseconds;

    @ManyToMany
    private List<TestCase> samples;

    @JsonIgnore
    @ManyToMany
    private List<TestCase> tests;

    @ElementCollection(targetClass = Long.class)
    @CollectionTable(name = "tagids", joinColumns = @JoinColumn(name = "problem_id"))
    @Column(name = "tag", nullable = false)
    private List<Long> tags;
}
