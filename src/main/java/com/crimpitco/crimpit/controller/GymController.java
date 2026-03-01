package com.crimpitco.crimpit.controller;

import com.crimpit.api.gyms.GymsApi;
import com.crimpit.dto.gyms.GymCreateRequest;
import com.crimpit.dto.gyms.GymResponse;
import com.crimpitco.crimpit.service.GymService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GymController implements GymsApi {

    private final GymService gymService;

    @Override
    public ResponseEntity<GymResponse> createGym(GymCreateRequest request) {
        GymResponse response = gymService.createGym(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Override
    public ResponseEntity<Void> deleteGym(Long gymId) {
        gymService.deleteGym(gymId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<GymResponse> modifyGym(Long gymId, GymCreateRequest gymCreateRequest) {
        GymResponse response = gymService.modifyGym(gymId, gymCreateRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @Override
    public ResponseEntity<List<GymResponse>> getGym(String city){
        List<GymResponse> response = gymService.getGym(city);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
