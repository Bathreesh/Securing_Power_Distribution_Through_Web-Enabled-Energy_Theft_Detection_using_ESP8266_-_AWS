package com.example.energytheft.controller;

import com.example.energytheft.entity.AppValue;
import com.example.energytheft.repository.AppValueRepository;
import com.example.energytheft.entity.EnergyReading;
import com.example.energytheft.repository.EnergyReadingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/simulate")
public class SimulationController {

    @Autowired
    private EnergyReadingRepository repository;

    @Autowired
    private AppValueRepository appValueRepository;

    // Endpoint to easy add data: /api/simulate/add?voltage=230&current=5
    @GetMapping("/add")
    public ResponseEntity<String> addReading(
            @RequestParam(defaultValue = "230.0") double voltage,
            @RequestParam double current,
            @RequestParam(defaultValue = "MTR006") String meterId) {

        double power = voltage * current;
        boolean theftDetected = current > 8.0;

        EnergyReading reading = new EnergyReading();
        reading.setMeterId(meterId);
        reading.setVoltage(voltage);
        reading.setCurrent(current);
        reading.setPower(power);
        reading.setTheftDetected(theftDetected);
        reading.setTimestamp(LocalDateTime.now());

        repository.save(reading);

        // Also update the dashboard current value
        AppValue v = appValueRepository.findById(1L).orElse(new AppValue());
        v.setId(1L);
        v.setVoltage(BigDecimal.valueOf(voltage));
        v.setCurrent(BigDecimal.valueOf(current));
        appValueRepository.save(v);

        return ResponseEntity.ok("Success: Data added to database. Check Dashboard.");
    }

    @PostMapping("/theft")
    public ResponseEntity<String> simulateTheft() {
        java.util.Random random = new java.util.Random();
        double voltage = 180 + random.nextDouble() * 20; // 180–200 V
        double current = 12 + random.nextDouble() * 8; // 12–20 A
        String meterId = "MTR_THEFT_" + (100 + random.nextInt(900));

        double power = voltage * current;
        boolean theftDetected = true; // Always true for this simulation

        EnergyReading reading = new EnergyReading();
        reading.setMeterId(meterId);
        reading.setVoltage(voltage);
        reading.setCurrent(current);
        reading.setPower(power);
        reading.setTheftDetected(theftDetected);
        reading.setTimestamp(LocalDateTime.now());

        repository.save(reading);

        // Update the dashboard current value to show the theft
        AppValue v = appValueRepository.findById(1L).orElse(new AppValue());
        v.setId(1L);
        v.setVoltage(BigDecimal.valueOf(voltage));
        v.setCurrent(BigDecimal.valueOf(current));
        appValueRepository.save(v);

        return ResponseEntity.ok("Theft Simulated: Voltage=" + String.format("%.2f", voltage) +
                "V, Current=" + String.format("%.2f", current) + "A. Check Dashboard!");
    }
}
