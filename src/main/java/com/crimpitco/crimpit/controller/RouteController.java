package com.crimpitco.crimpit.controller;

import com.crimpit.api.routes.RoutesApi;
import com.crimpit.dto.routes.RouteCreateRequest;
import com.crimpit.dto.routes.RouteResponse;
import com.crimpitco.crimpit.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RouteController implements RoutesApi {

    private final RouteService routeService;

    @Override
    public ResponseEntity<RouteResponse> createRoute(Long gymId, RouteCreateRequest request) {
        RouteResponse response = routeService.createRoute(gymId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<List<RouteResponse>> listRoutes(Long gymId) {
        return ResponseEntity.ok(routeService.listRoutes(gymId));
    }

    @Override
    public ResponseEntity<RouteResponse> modifyRoute(Long routeId, RouteCreateRequest routeCreateRequest) {
        RouteResponse response = routeService.modifyRoute(routeId, routeCreateRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @Override
    public ResponseEntity<Void> deleteRoute(Long routeId) {
        routeService.deleteRoute(routeId);
        return ResponseEntity.noContent().build();
    }
}
