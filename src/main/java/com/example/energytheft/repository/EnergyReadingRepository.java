package com.example.energytheft.repository;

import com.example.energytheft.entity.EnergyReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnergyReadingRepository
        extends JpaRepository<EnergyReading, Long> {

    EnergyReading findTopByOrderByTimestampDesc();

    EnergyReading findTopByMeterIdOrderByTimestampDesc(String meterId);

    List<EnergyReading> findByTheftDetectedTrue();
}
