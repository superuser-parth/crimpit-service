package com.crimpitco.crimpit.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "routes")
@Getter @Setter @NoArgsConstructor
// The Magic for Soft Deletes:
@SQLDelete(sql = "UPDATE routes SET is_active = false WHERE id = ?")
@SQLRestriction("is_active = true")
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Optional

    @Column(nullable = false)
    private String grade; // e.g., "V5"

    @Column(nullable = false)
    private String color;

    private String setterName;

    private String imageUrl;       // S3 Link
    private String officialBetaUrl; // S3 Link

    @Column(name = "is_active")
    private boolean isActive = true;

    // Many Routes belong to One Gym
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gym_id", nullable = false)
    private Gym gym;

    // Many-to-Many with Tags
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "route_tags",
            joinColumns = @JoinColumn(name = "route_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    // Helper method to add tags safely
    public void addTag(Tag tag) {
        this.tags.add(tag);
        tag.getRoutes().add(this);
    }
}