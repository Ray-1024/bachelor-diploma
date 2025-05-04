package ray1024.articleservice.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ray1024.articleservice.model.entity.Article;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ArticleResponse {
    private Article article;
}
