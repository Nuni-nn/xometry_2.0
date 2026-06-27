package com.nuray.manufacturing.material.repository;

import com.nuray.manufacturing.material.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MaterialRepository extends JpaRepository<Material, UUID> {

    List<Material> findAllByActiveTrueOrderByNameAsc();

    Optional<Material> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);
}
