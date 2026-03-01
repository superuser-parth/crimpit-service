package com.crimpitco.crimpit.service;

import com.crimpit.dto.gyms.GymCreateRequest;
import com.crimpit.dto.gyms.GymResponse;
import com.crimpitco.crimpit.model.entity.Gym;
import com.crimpitco.crimpit.repository.GymRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.util.StringUtils;
import java.util.List;
import java.util.stream.Collectors;

import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class GymService {

    private final GymRepository gymRepository;

    @Transactional
    public GymResponse createGym(GymCreateRequest request) {
        Gym gymEntity = new Gym();
        gymEntity.setName(request.getName());
        gymEntity.setLocation(request.getLocation());
        gymEntity.setCity(request.getCity());
        gymEntity.setAdminUserId(request.getAdminUserId());
        gymEntity.setRecordStat("A");

        Gym savedGym = gymRepository.save(gymEntity);
        return mapToResponse(savedGym);
    }

    @Transactional
    public void deleteGym(Long gymId) {
        Gym gymRecord = gymRepository.findById(gymId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gym not found"));

        gymRepository.delete(gymRecord);
    }

    @Transactional
    public GymResponse modifyGym(Long gymId, GymCreateRequest gymCreateRequest) {
        Gym gymRecord = gymRepository.findById(gymId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gym not found"));

        if ("D".equalsIgnoreCase(gymRecord.getRecordStat())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gym has been deleted");
        }
        gymRecord.setCity(gymCreateRequest.getCity());
        gymRecord.setLocation(gymCreateRequest.getLocation());
        gymRecord.setName(gymCreateRequest.getName());

        gymRepository.save(gymRecord);

        return mapToResponse(gymRecord);
    }

    @Transactional(readOnly = true)
    public List<GymResponse> getGym(String city) {
        List<Gym> gymRecords;

        // StringUtils.hasText() safely checks if it's null, empty, or just blank spaces
        if (!StringUtils.hasText(city)) {
            gymRecords = gymRepository.findByRecordStat("A");
        } else {
            gymRecords = gymRepository.findByCity(city);
        }

        // Convert the List of Gym Entities into a List of GymResponse DTOs
        return gymRecords.stream()
                .map(this::mapToResponse) // Assuming you have a private method for this
                .collect(Collectors.toList()); // Use .toList() if you are on Java 16+
    }

    private GymResponse mapToResponse(Gym gym) {
        GymResponse response = new GymResponse();
        response.setId(gym.getId());
        response.setName(gym.getName());
        response.setLocation(gym.getLocation());
        response.setCity(gym.getCity());
        response.setAdminUserId(gym.getAdminUserId());

        if (gym.getCreatedAt() != null) {
            response.setCreatedAt(gym.getCreatedAt().atOffset(ZoneOffset.UTC));
        }
        if (gym.getUpdatedAt() != null) {
            response.setUpdatedAt(gym.getUpdatedAt().atOffset(ZoneOffset.UTC));
        }

        return response;
    }
}
