package com.nuray.manufacturing.common.config;

import com.nuray.manufacturing.material.entity.Material;
import com.nuray.manufacturing.material.entity.MaterialCategory;
import com.nuray.manufacturing.material.repository.MaterialRepository;
import com.nuray.manufacturing.technology.entity.Technology;
import com.nuray.manufacturing.technology.entity.TechnologyCode;
import com.nuray.manufacturing.technology.repository.TechnologyRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedReferenceData(
            MaterialRepository materialRepository,
            TechnologyRepository technologyRepository
    ) {
        return args -> {
            seedMaterial(materialRepository, "Aluminum 6061", MaterialCategory.METAL, "2.700", "7.50");
            seedMaterial(materialRepository, "Stainless Steel 316", MaterialCategory.METAL, "8.000", "12.50");
            seedMaterial(materialRepository, "ABS Plastic", MaterialCategory.PLASTIC, "1.040", "3.20");
            seedMaterial(materialRepository, "Nylon", MaterialCategory.PLASTIC, "1.150", "4.10");

            seedTechnology(technologyRepository, "CNC Milling", TechnologyCode.CNC_MILLING, "50.00");
            seedTechnology(technologyRepository, "CNC Turning", TechnologyCode.CNC_TURNING, "45.00");
            seedTechnology(technologyRepository, "3D Printing", TechnologyCode.THREE_D_PRINTING, "25.00");
            seedTechnology(technologyRepository, "Laser Cutting", TechnologyCode.LASER_CUTTING, "35.00");
        };
    }

    private void seedMaterial(
            MaterialRepository materialRepository,
            String name,
            MaterialCategory category,
            String density,
            String pricePerKg
    ) {
        if (materialRepository.existsByNameIgnoreCase(name)) {
            return;
        }

        Material material = new Material();
        material.setName(name);
        material.setCategory(category);
        material.setDensity(new BigDecimal(density));
        material.setPricePerKg(new BigDecimal(pricePerKg));
        material.setActive(true);
        materialRepository.save(material);
    }

    private void seedTechnology(
            TechnologyRepository technologyRepository,
            String name,
            TechnologyCode code,
            String basePrice
    ) {
        if (technologyRepository.existsByCode(code)) {
            return;
        }

        Technology technology = new Technology();
        technology.setName(name);
        technology.setCode(code);
        technology.setBasePrice(new BigDecimal(basePrice));
        technology.setActive(true);
        technologyRepository.save(technology);
    }
}
