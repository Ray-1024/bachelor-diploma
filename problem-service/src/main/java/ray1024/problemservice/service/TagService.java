package ray1024.problemservice.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ray1024.problemservice.exception.TagAlreadyExistsException;
import ray1024.problemservice.exception.TagNotFoundException;
import ray1024.problemservice.model.entity.Tag;
import ray1024.problemservice.repository.TagRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class TagService {
    private final TagRepository tagRepository;

    public List<Tag> getAll(int page, int size) {
        return tagRepository.findAll(Pageable.ofSize(size).withPage(page)).getContent();
    }

    public Tag getById(long id) {
        return tagRepository.findById(id).orElseThrow(() -> new TagNotFoundException("Tag not found"));
    }

    @Transactional
    public Tag updateById(long id, @NonNull String name) {
        if (tagRepository.findByName(name).isEmpty())
            throw new TagNotFoundException("Tag not found");
        return tagRepository.save(Tag.builder().name(name).build());
    }

    public void deleteById(long id) {
        tagRepository.deleteById(id);
    }

    @Transactional
    public Tag create(@NonNull String name) {
        if (tagRepository.findByName(name).isPresent())
            throw new TagAlreadyExistsException("Tag with name " + name + " already exists");
        return tagRepository.save(Tag.builder().name(name).build());
    }
}
