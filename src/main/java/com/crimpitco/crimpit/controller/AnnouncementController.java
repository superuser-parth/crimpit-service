package com.crimpitco.crimpit.controller;

import com.crimpit.api.announcements.AnnouncementsApi;
import com.crimpit.dto.announcements.AnnouncementCreateRequest;
import com.crimpit.dto.announcements.AnnouncementResponse;
import com.crimpitco.crimpit.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AnnouncementController implements AnnouncementsApi {

    private final AnnouncementService announcementService;

    @Override
    public ResponseEntity<AnnouncementResponse> createAnnouncement(Long gymId, AnnouncementCreateRequest request) {
        AnnouncementResponse response = announcementService.createAnnouncement(gymId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<List<AnnouncementResponse>> listAnnouncements(Long gymId) {
        return ResponseEntity.ok(announcementService.listAnnouncements(gymId));
    }
}
