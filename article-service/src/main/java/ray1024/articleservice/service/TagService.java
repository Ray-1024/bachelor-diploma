package ray1024.articleservice.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import ray1024.articleservice.model.entity.Tag;
import ray1024.articleservice.repository.TagRepository;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class TagService {
    private TagRepository tagRepository;

    public List<Tag> fromStringTags(@NonNull List<String> tags) {
        return tags.stream()
                .map(tagRepository::findByNameIgnoreCase)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }
}
