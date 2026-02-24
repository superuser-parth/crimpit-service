package com.crimpitco.crimpit.repository;

import com.crimpitco.crimpit.model.entity.Gym;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GymRepository extends JpaRepository<Gym, Long> {

    // Custom finder method
    // Spring generates the SQL: "SELECT * FROM gyms WHERE city = ?" automatically
    List<Gym> findByCity(String city);

    // Another example: Find by admin user to see all gyms owned by one person
    List<Gym> findByAdminUserId(Long adminUserId);

    List<Gym> findByRecordStat(String recordStat);
}