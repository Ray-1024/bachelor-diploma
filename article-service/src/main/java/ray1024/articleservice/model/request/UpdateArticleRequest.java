package ray1024.articleservice.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UpdateArticleRequest {
    private Long id;
    private String title;
    private String article;
    private List<String> tags;
}
