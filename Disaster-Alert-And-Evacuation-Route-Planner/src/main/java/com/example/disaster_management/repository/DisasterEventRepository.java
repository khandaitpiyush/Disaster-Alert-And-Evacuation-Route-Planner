package com.example.disaster_management.repository;

import com.example.disaster_management.model.DisasterEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA Repository for the DisasterEvent entity.
 * It inherits CRUD operations (like findAll, save, findById) automatically.
 */
@Repository
public interface DisasterEventRepository extends JpaRepository<DisasterEvent, Long> {

    // Spring Data JPA automatically provides common methods like:
    // List<DisasterEvent> findAll();
    // DisasterEvent save(DisasterEvent event);
    // Optional<DisasterEvent> findById(Long id);

    // No custom code is required here unless specific queries are needed.
}