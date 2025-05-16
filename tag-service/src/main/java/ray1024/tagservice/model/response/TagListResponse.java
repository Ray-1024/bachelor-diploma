package ray1024.tagservice.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ray1024.tagservice.model.entity.Tag;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TagListResponse {
    private List<Tag> tags;
    private Integer page;
    private Integer size;
}
