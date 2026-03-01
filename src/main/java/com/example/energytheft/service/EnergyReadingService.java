package com.example.energytheft.service;

import com.example.energytheft.entity.EnergyReading;
import com.example.energytheft.repository.EnergyReadingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnergyReadingService {

    @Autowired
    private EnergyReadingRepository repository;

    public EnergyReading getLatestReading(String meterId) {
        return repository.findTopByMeterIdOrderByTimestampDesc(meterId);
    }
}
