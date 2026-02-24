package com.crimpitco.crimpit.service;

import com.crimpit.dto.tags.TagCreateRequest;
import com.crimpit.dto.tags.TagResponse;
import com.crimpitco.crimpit.model.entity.Tag;
import com.crimpitco.crimpit.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    @Transactional
    public TagResponse createTag(TagCreateRequest request) {
        String normalizedName = normalizeName(request.getName());
        if (normalizedName.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tag name is required");
        }

        if (tagRepository.findByNameIgnoreCase(normalizedName).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Tag already exists");
        }

        Tag tag = new Tag();
        tag.setName(normalizedName);
        Tag savedTag = tagRepository.save(tag);

        return mapToResponse(savedTag);
    }

    @Transactional(readOnly = true)
    public List<TagResponse> listTags() {
        return tagRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    private TagResponse mapToResponse(Tag tag) {
        TagResponse response = new TagResponse();
        response.setId(tag.getId());
        response.setName(tag.getName());
        return response;
    }

    private String normalizeName(String name) {
        return name == null ? "" : name.trim();
    }
}
