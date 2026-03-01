package com.example.energytheft.controller;

import com.example.energytheft.entity.EnergyReading;
import com.example.energytheft.repository.EnergyReadingRepository;
import com.example.energytheft.service.AppValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class TelemetryController {

    @Autowired
    private AppValueService appValueService;

    @Autowired
    private EnergyReadingRepository energyReadingRepository;

    @PostMapping("/telemetry")
    public ResponseEntity<String> receiveTelemetry(@RequestBody TelemetryData data) {
        try {
            // 1. Calculate Current if missing (Power / Voltage)
            BigDecimal voltage = BigDecimal.valueOf(data.getVoltage());
            BigDecimal power = BigDecimal.valueOf(data.getPower());
            BigDecimal current = BigDecimal.ZERO;

            if (data.getVoltage() != 0) {
                current = power.divide(voltage, 4, java.math.RoundingMode.HALF_UP);
            }

            // 2. Update Live Dashboard Values (AppValueService)
            appValueService.updateValues(voltage, current, BigDecimal.valueOf(data.getEnergy()));

            // 3. Save to History (EnergyReadingRepository)
            EnergyReading reading = new EnergyReading();
            reading.setVoltage(data.getVoltage());
            reading.setPower(data.getPower());
            reading.setCurrent(current.doubleValue());
            reading.setMeterId("ESP8266"); // Default Meter ID
            reading.setTimestamp(LocalDateTime.now());

            // Set theft status based on message or thresholds
            boolean theft = "THEFT".equalsIgnoreCase(data.getStatus());
            reading.setTheftDetected(theft);

            energyReadingRepository.save(reading);

            return ResponseEntity.ok("Data received");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error processing data");
        }
    }

    // Inner DTO Class
    public static class TelemetryData {
        private double voltage;
        private double power;
        private double energy;
        private String status;

        // Getters and Setters
        public double getVoltage() {
            return voltage;
        }

        public void setVoltage(double voltage) {
            this.voltage = voltage;
        }

        public double getPower() {
            return power;
        }

        public void setPower(double power) {
            this.power = power;
        }

        public double getEnergy() {
            return energy;
        }

        public void setEnergy(double energy) {
            this.energy = energy;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
