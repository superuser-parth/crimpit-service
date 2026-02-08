package com.crimpitco.crimpit.controller;

import com.crimpit.api.gyms.GymsApi;
import com.crimpit.dto.gyms.GymCreateRequest;
import com.crimpit.dto.gyms.GymResponse;
import com.crimpitco.crimpit.service.GymService ;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GymController implements GymsApi {

    private final GymService gymService;

    @Override
    public ResponseEntity<GymResponse> createGym(GymCreateRequest request) {
        // The validation (@NotNull, @Size) has already happened automatically
        // before reaching this line, thanks to the generated interface.

        GymResponse response = gymService.createGym(request);

        return ResponseEntity
                .status(HttpStatus.CREATED) // Returns 201 Created
                .body(response);
    }
}