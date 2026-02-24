package com.crimpitco.crimpit.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "gyms")
@Getter @Setter @NoArgsConstructor // Lombok handles boilerplate
public class Gym {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String location;

    @Column(nullable = false)
    private String city;

    // LOGICAL Foreign Key (This ID lives in the Auth Service)
    @Column(name = "admin_user_id", nullable = false)
    private Long adminUserId;

    @Column(name = "record_stat", nullable = false, length = 1)
    private String recordStat = "A";

    // One Gym has Many Routes
    // 'orphanRemoval = true' means if you remove a route from this list, it deletes from DB
    @OneToMany(mappedBy = "gym", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Route> routes = new ArrayList<>();

    @OneToMany(mappedBy = "gym", cascade = CascadeType.ALL)
    private List<Announcement> announcements = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
