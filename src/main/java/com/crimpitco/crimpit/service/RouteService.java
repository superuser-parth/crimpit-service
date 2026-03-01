package com.crimpitco.crimpit.service;

import com.crimpit.dto.routes.RouteCreateRequest;
import com.crimpit.dto.routes.RouteResponse;
import com.crimpitco.crimpit.model.entity.Gym;
import com.crimpitco.crimpit.model.entity.Route;
import com.crimpitco.crimpit.model.entity.Tag;
import com.crimpitco.crimpit.repository.GymRepository;
import com.crimpitco.crimpit.repository.RouteRepository;
import com.crimpitco.crimpit.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZoneOffset;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;
    private final GymRepository gymRepository;
    private final TagRepository tagRepository;

    @Transactional
    public RouteResponse createRoute(Long gymId, RouteCreateRequest request) {
        Gym gym = loadActiveGym(gymId);

        Route route = new Route();
        route.setGym(gym);
        applyRequestToRoute(route, request);
        route.setActive(true);

        Route savedRoute = routeRepository.save(route);
        return mapToResponse(savedRoute);
    }

    @Transactional
    public RouteResponse modifyRoute(Long routeId, RouteCreateRequest request) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Route not found"));

        applyRequestToRoute(route, request);
        Route savedRoute = routeRepository.save(route);
        return mapToResponse(savedRoute);
    }

    @Transactional(readOnly = true)
    public List<RouteResponse> listRoutes(Long gymId) {
        loadActiveGym(gymId);

        return routeRepository.findByGymId(gymId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public void deleteRoute(Long routeId) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Route not found"));
        routeRepository.delete(route);
    }

    private RouteResponse mapToResponse(Route route) {
        RouteResponse response = new RouteResponse();
        response.setId(route.getId());
        response.setGymId(route.getGym().getId());
        response.setName(route.getName());
        response.setSetterName(route.getSetterName());
        response.setImageUrl(route.getImageUrl());
        response.setOfficialBetaUrl(route.getOfficialBetaUrl());
        response.setIsActive(route.isActive());
        response.setTagNames(route.getTags().stream().map(Tag::getName).toList());

        if (route.getCreatedAt() != null) {
            response.setCreatedAt(route.getCreatedAt().atOffset(ZoneOffset.UTC));
        }

        return response;
    }

    //Checks if tags exist in tags table, If not creates the new tags
    private Set<Tag> resolveTags(List<String> tagNames) {
        if (tagNames == null || tagNames.isEmpty()) {
            return Set.of();
        }

        Set<String> normalizedNames = new LinkedHashSet<>();
        for (String name : tagNames) {
            if (name == null) {
                continue;
            }
            String trimmed = name.trim();
            if (!trimmed.isEmpty()) {
                normalizedNames.add(trimmed);
            }
        }

        if (normalizedNames.isEmpty()) {
            return Set.of();
        }

        Set<Tag> tags = new LinkedHashSet<>();
        for (String name : normalizedNames) {
            Tag tag = tagRepository.findByNameIgnoreCase(name)
                    .orElseGet(() -> {
                        Tag created = new Tag();
                        created.setName(name);
                        return tagRepository.save(created);
                    });
            tags.add(tag);
        }

        return tags;
    }

    private void applyRequestToRoute(Route route, RouteCreateRequest request) {
        route.setName(request.getName());
        route.setSetterName(request.getSetterName());
        route.setImageUrl(request.getImageUrl());
        route.setOfficialBetaUrl(request.getOfficialBetaUrl());
        replaceTags(route, resolveTags(request.getTagNames()));
    }

    //Handles many-to-many relationship between tags and routes specifically designed for update reqs
    private void replaceTags(Route route, Set<Tag> newTags) {
        for (Tag existingTag : route.getTags()) {
            existingTag.getRoutes().remove(route); //Remove tags association with routes
        }
        route.getTags().clear(); //Removes all tags from the current route

        for (Tag newTag : newTags) {
            route.addTag(newTag);
        }
    }

    private Gym loadActiveGym(Long gymId) {
        Gym gym = gymRepository.findById(gymId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gym not found"));

        if ("D".equalsIgnoreCase(gym.getRecordStat())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gym has been deleted");
        }

        return gym;
    }
}
