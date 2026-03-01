package com.example.energytheft.controller;

import com.example.energytheft.entity.TheftAlert;
import com.example.energytheft.repository.TheftAlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST API Controller for Theft Alerts
 * Handles alert management and retrieval
 */
@RestController
@RequestMapping("/api/alerts")

public class AlertApiController {

    @Autowired
    private TheftAlertRepository alertRepository;

    /**
     * GET /api/alerts - Get all alerts ordered by timestamp
     */
    @GetMapping
    public ResponseEntity<List<TheftAlert>> getAllAlerts() {
        return ResponseEntity.ok(alertRepository.findAllByOrderByTimestampDesc());
    }

    /**
     * GET /api/alerts/unresolved - Get all unresolved alerts
     */
    @GetMapping("/unresolved")
    public ResponseEntity<List<TheftAlert>> getUnresolvedAlerts() {
        return ResponseEntity.ok(alertRepository.findByResolved(false));
    }

    /**
     * GET /api/alerts/meter/{meterId} - Get alerts for a specific meter
     */
    @GetMapping("/meter/{meterId}")
    public ResponseEntity<List<TheftAlert>> getAlertsByMeter(@PathVariable @NonNull String meterId) {
        return ResponseEntity.ok(alertRepository.findByMeterId(meterId));
    }

    /**
     * PUT /api/alerts/{id}/resolve - Mark an alert as resolved
     */
    @PutMapping("/{id}/resolve")
    public ResponseEntity<?> resolveAlert(@PathVariable @NonNull Long id) {
        Optional<TheftAlert> alertOpt = alertRepository.findById(id);
        if (alertOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        TheftAlert alert = alertOpt.get();
        alert.setResolved(true);
        alertRepository.save(alert);
        return ResponseEntity.ok(Map.of("message", "Alert resolved successfully"));
    }

    /**
     * POST /api/alerts - Create a new alert
     */
    @PostMapping
    public ResponseEntity<TheftAlert> createAlert(@RequestBody @NonNull TheftAlert alert) {
        TheftAlert saved = alertRepository.save(alert);
        return ResponseEntity.ok(saved);
    }

    /**
     * GET /api/alerts/status - Get system status
     */
    @GetMapping("/status")
    public ResponseEntity<?> getSystemStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("online", true);
        status.put("timestamp", LocalDateTime.now());
        return ResponseEntity.ok(status);
    }
}
