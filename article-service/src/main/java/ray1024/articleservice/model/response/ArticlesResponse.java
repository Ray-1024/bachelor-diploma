package ray1024.articleservice.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ray1024.articleservice.model.entity.Article;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ArticlesResponse {
    private List<Article> articles;
}
