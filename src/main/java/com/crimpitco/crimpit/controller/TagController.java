package com.crimpitco.crimpit.controller;

import com.crimpit.api.tags.TagsApi;
import com.crimpit.dto.tags.TagCreateRequest;
import com.crimpit.dto.tags.TagResponse;
import com.crimpitco.crimpit.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TagController implements TagsApi {

    private final TagService tagService;

    @Override
    public ResponseEntity<TagResponse> createTag(TagCreateRequest request) {
        TagResponse response = tagService.createTag(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<List<TagResponse>> listTags() {
        return ResponseEntity.ok(tagService.listTags());
    }
}
