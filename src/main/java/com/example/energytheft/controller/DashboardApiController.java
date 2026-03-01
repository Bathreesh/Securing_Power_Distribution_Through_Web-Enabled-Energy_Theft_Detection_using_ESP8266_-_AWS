package com.example.energytheft.controller;

import com.example.energytheft.dto.ApiResponse;
import com.example.energytheft.dto.DashboardSummary;
import com.example.energytheft.entity.EnergyReading;
import com.example.energytheft.repository.EnergyReadingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST API Controller for Dashboard
 * Provides summary statistics and energy data for the dashboard
 */
@RestController
@RequestMapping("/api/dashboard")

public class DashboardApiController {

    @Autowired
    private EnergyReadingRepository energyReadingRepository;

    /**
     * GET /api/dashboard/summary
     * Get dashboard summary statistics
     */
    @GetMapping("/summary")
    public ResponseEntity<DashboardSummary> getDashboardSummary() {
        DashboardSummary summary = new DashboardSummary();

        List<EnergyReading> allReadings = energyReadingRepository.findAll();
        List<EnergyReading> theftReadings = energyReadingRepository.findByTheftDetectedTrue();

        summary.setTotalReadings(allReadings.size());
        summary.setTheftDetectedCount(theftReadings.size());
        summary.setAlertCount(theftReadings.size()); // Alerts = theft detections

        if (!allReadings.isEmpty()) {
            double avgVoltage = allReadings.stream()
                    .mapToDouble(EnergyReading::getVoltage)
                    .average()
                    .orElse(0.0);
            double avgCurrent = allReadings.stream()
                    .mapToDouble(EnergyReading::getCurrent)
                    .average()
                    .orElse(0.0);
            double avgPower = allReadings.stream()
                    .mapToDouble(EnergyReading::getPower)
                    .average()
                    .orElse(0.0);

            summary.setAverageVoltage(Math.round(avgVoltage * 100.0) / 100.0);
            summary.setAverageCurrent(Math.round(avgCurrent * 100.0) / 100.0);
            summary.setAveragePower(Math.round(avgPower * 100.0) / 100.0);
        }

        return ResponseEntity.ok(summary);
    }

    /**
     * GET /api/dashboard/latest/{meterId}
     * Get latest reading for a specific meter
     */
    @GetMapping("/latest/{meterId}")
    public ResponseEntity<?> getLatestReading(@PathVariable String meterId) {
        EnergyReading reading = energyReadingRepository.findTopByMeterIdOrderByTimestampDesc(meterId);

        if (reading == null) {
            // Return default reading if none exists
            EnergyReading defaultReading = new EnergyReading();
            defaultReading.setMeterId(meterId);
            defaultReading.setVoltage(0.0);
            defaultReading.setCurrent(0.0);
            defaultReading.setPower(0.0);
            defaultReading.setTheftDetected(false);
            return ResponseEntity.ok(defaultReading);
        }

        return ResponseEntity.ok(reading);
    }

    /**
     * GET /api/dashboard/readings
     * Get all energy readings for charts
     */
    @GetMapping("/readings")
    public ResponseEntity<List<EnergyReading>> getAllReadings() {
        List<EnergyReading> readings = energyReadingRepository.findAll();
        return ResponseEntity.ok(readings);
    }

    /**
     * GET /api/dashboard/alerts
     * Get all theft alerts
     */
    @GetMapping("/alerts")
    public ResponseEntity<?> getAlerts() {
        List<EnergyReading> theftReadings = energyReadingRepository.findByTheftDetectedTrue();
        return ResponseEntity.ok(ApiResponse.success("Alerts retrieved", theftReadings));
    }
}
