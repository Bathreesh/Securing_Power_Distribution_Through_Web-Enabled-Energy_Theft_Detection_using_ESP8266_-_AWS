package com.example.energytheft.controller;

import com.example.energytheft.dto.ApiResponse;
import com.example.energytheft.entity.EnergyReading;
import com.example.energytheft.repository.EnergyReadingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST API Controller for Energy Readings
 * Handles CRUD operations for energy meter data
 */
@RestController
@RequestMapping("/api/energy")

public class EnergyApiController {

    @Autowired
    private EnergyReadingRepository energyReadingRepository;

    // Threshold values for theft detection
    private static final double MIN_VOLTAGE = 180.0;
    private static final double MAX_CURRENT = 50.0;

    /**
     * GET /api/energy/readings - Get all energy readings
     */
    @GetMapping("/readings")
    public ResponseEntity<List<EnergyReading>> getAllReadings() {
        return ResponseEntity.ok(energyReadingRepository.findAll());
    }

    /**
     * GET /api/energy/readings/{id} - Get a specific reading by ID
     */
    @GetMapping("/readings/{id}")
    public ResponseEntity<?> getReadingById(@PathVariable @NonNull Long id) {
        Optional<EnergyReading> reading = energyReadingRepository.findById(id);
        if (reading.isPresent()) {
            return ResponseEntity.ok(reading.get());
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * POST /api/energy/readings - Create a new energy reading
     */
    @PostMapping("/readings")
    public ResponseEntity<?> createReading(@RequestBody EnergyReading reading) {
        try {
            // Calculate power if not provided
            if (reading.getPower() == 0) {
                reading.setPower(reading.getVoltage() * reading.getCurrent());
            }

            // Auto-detect theft based on thresholds
            boolean theftDetected = reading.getVoltage() < MIN_VOLTAGE ||
                    reading.getCurrent() > MAX_CURRENT;
            reading.setTheftDetected(theftDetected);

            // Set timestamp if not provided
            if (reading.getTimestamp() == null) {
                reading.setTimestamp(LocalDateTime.now());
            }

            EnergyReading saved = energyReadingRepository.save(reading);
            return ResponseEntity.ok(ApiResponse.success("Reading created", saved));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to create reading: " + e.getMessage()));
        }
    }

    /**
     * PUT /api/energy/readings/{id} - Update an existing reading
     */
    @PutMapping("/readings/{id}")
    public ResponseEntity<?> updateReading(@PathVariable @NonNull Long id, @RequestBody EnergyReading reading) {
        Optional<EnergyReading> existingOpt = energyReadingRepository.findById(id);
        if (existingOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        EnergyReading existing = existingOpt.get();
        existing.setVoltage(reading.getVoltage());
        existing.setCurrent(reading.getCurrent());
        existing.setPower(reading.getVoltage() * reading.getCurrent());
        existing.setTheftDetected(
                reading.getVoltage() < MIN_VOLTAGE || reading.getCurrent() > MAX_CURRENT);
        existing.setTimestamp(LocalDateTime.now());

        EnergyReading updated = energyReadingRepository.save(existing);
        return ResponseEntity.ok(ApiResponse.success("Reading updated", updated));
    }

    /**
     * DELETE /api/energy/readings/{id} - Delete a reading
     */
    @DeleteMapping("/readings/{id}")
    public ResponseEntity<?> deleteReading(@PathVariable @NonNull Long id) {
        if (energyReadingRepository.existsById(id)) {
            energyReadingRepository.deleteById(id);
            return ResponseEntity.ok(ApiResponse.success("Reading deleted"));
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * GET /api/energy/theft - Get all readings with theft detected
     */
    @GetMapping("/theft")
    public ResponseEntity<List<EnergyReading>> getTheftReadings() {
        return ResponseEntity.ok(energyReadingRepository.findByTheftDetectedTrue());
    }

    /**
     * POST /api/energy/simulate - Simulate energy readings for testing
     */
    @PostMapping("/simulate")
    public ResponseEntity<?> simulateReading(
            @RequestParam String meterId,
            @RequestParam double voltage,
            @RequestParam double current) {

        EnergyReading reading = new EnergyReading();
        reading.setMeterId(meterId);
        reading.setVoltage(voltage);
        reading.setCurrent(current);
        reading.setPower(voltage * current);
        reading.setTheftDetected(voltage < MIN_VOLTAGE || current > MAX_CURRENT);
        reading.setTimestamp(LocalDateTime.now());

        EnergyReading saved = energyReadingRepository.save(reading);
        return ResponseEntity.ok(ApiResponse.success("Simulation created", saved));
    }
}
