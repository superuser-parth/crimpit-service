package com.crimpitco.crimpit.service;

import com.crimpit.dto.announcements.AnnouncementCreateRequest;
import com.crimpit.dto.announcements.AnnouncementResponse;
import com.crimpitco.crimpit.model.entity.Announcement;
import com.crimpitco.crimpit.model.entity.Gym;
import com.crimpitco.crimpit.repository.AnnouncementRepository;
import com.crimpitco.crimpit.repository.GymRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final GymRepository gymRepository;

    @Transactional
    public AnnouncementResponse createAnnouncement(Long gymId, AnnouncementCreateRequest request) {
        Gym gym = gymRepository.findById(gymId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gym not found"));

        Announcement announcement = new Announcement();
        announcement.setGym(gym);
        announcement.setTitle(request.getTitle());
        announcement.setContent(request.getContent());

        Announcement savedAnnouncement = announcementRepository.save(announcement);
        return mapToResponse(savedAnnouncement);
    }

    @Transactional(readOnly = true)
    public List<AnnouncementResponse> listAnnouncements(Long gymId) {
        if (!gymRepository.existsById(gymId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gym not found");
        }

        return announcementRepository.findByGymIdOrderByCreatedAtDesc(gymId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    private AnnouncementResponse mapToResponse(Announcement announcement) {
        AnnouncementResponse response = new AnnouncementResponse();
        response.setId(announcement.getId());
        response.setGymId(announcement.getGym().getId());
        response.setTitle(announcement.getTitle());
        response.setContent(announcement.getContent());

        if (announcement.getCreatedAt() != null) {
            response.setCreatedAt(announcement.getCreatedAt().atOffset(ZoneOffset.UTC));
        }

        return response;
    }
}
