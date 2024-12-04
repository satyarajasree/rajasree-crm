package com.rajasreeit.backend.repo;

import com.rajasreeit.backend.entities.Property;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyRepo extends JpaRepository<Property, Long> {
}
