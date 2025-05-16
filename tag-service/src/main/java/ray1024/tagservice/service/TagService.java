package ray1024.tagservice.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ray1024.tagservice.exception.TagNotFoundException;
import ray1024.tagservice.model.entity.Tag;
import ray1024.tagservice.repository.TagRepository;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class TagService {
    private final TagRepository tagRepository;

    public List<Tag> fromStringTags(@NonNull List<String> tags) {
        return tags.stream()
                .map(tagRepository::findByNameIgnoreCase)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    public List<Tag> getAll(int page, int size) {
        return tagRepository.findAll(Pageable.ofSize(size).withPage(page)).getContent();
    }

    public Tag getById(long id) {
        return tagRepository.findById(id).orElseThrow(() -> new TagNotFoundException("Tag not found"));
    }
}
