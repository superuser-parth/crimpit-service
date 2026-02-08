package com.crimpitco.crimpit.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore; // Prevent infinite JSON loops

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tags")
@Getter @Setter @NoArgsConstructor
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name; // e.g., "Slab"

    // Inverse side of the relationship
    @ManyToMany(mappedBy = "tags")
    @JsonIgnore // Important: Don't try to serialize all routes when asking for a tag
    private Set<Route> routes = new HashSet<>();
}