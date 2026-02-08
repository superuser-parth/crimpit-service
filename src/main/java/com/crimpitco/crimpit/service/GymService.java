package com.crimpitco.crimpit.service;

import com.crimpit.dto.gyms.GymCreateRequest;
import com.crimpit.dto.gyms.GymResponse;
import com.crimpitco.crimpit.model.entity.Gym ; // Your JPA Entity
import com.crimpitco.crimpit.repository.GymRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class GymService {

    private final GymRepository gymRepository;

    public GymResponse createGym(GymCreateRequest request) {
        // 1. Map DTO -> Entity
        Gym gymEntity = new Gym();
        gymEntity.setName(request.getName());
        gymEntity.setLocation(request.getLocation());
        gymEntity.setCity(request.getCity());
        gymEntity.setAdminUserId(request.getAdminUserId());

        // 2. Save to DB
        Gym savedGym = gymRepository.save(gymEntity);

        // 3. Map Entity -> DTO (Response)
        return mapToResponse(savedGym);
    }

    private GymResponse mapToResponse(Gym gym) {
        GymResponse response = new GymResponse();
        response.setId(gym.getId());
        response.setName(gym.getName());
        response.setLocation(gym.getLocation());
        response.setCity(gym.getCity());
        response.setAdminUserId(gym.getAdminUserId());

        // Handle Timestamp conversion if necessary (SQL Timestamp -> OffsetDateTime)
        if (gym.getCreatedAt() != null) {
            response.setCreatedAt(gym.getCreatedAt().atOffset(ZoneOffset.UTC));
        }

        return response;
    }
}