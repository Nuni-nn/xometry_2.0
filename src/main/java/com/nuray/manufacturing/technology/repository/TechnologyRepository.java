package com.nuray.manufacturing.technology.repository;

import com.nuray.manufacturing.technology.entity.Technology;
import com.nuray.manufacturing.technology.entity.TechnologyCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TechnologyRepository extends JpaRepository<Technology, UUID> {

    List<Technology> findAllByActiveTrueOrderByNameAsc();

    Optional<Technology> findByNameIgnoreCase(String name);

    Optional<Technology> findByCode(TechnologyCode code);

    boolean existsByNameIgnoreCase(String name);

    boolean existsByCode(TechnologyCode code);
}
