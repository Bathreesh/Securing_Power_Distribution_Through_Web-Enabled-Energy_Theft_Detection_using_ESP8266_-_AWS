package com.example.energytheft.controller;

import com.example.energytheft.entity.AppValue;
import com.example.energytheft.repository.AppValueRepository;
import com.example.energytheft.service.AppValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/dashboard")
public class DashboardValueController {

    @Autowired
    private AppValueRepository repo;

    @Autowired
    private AppValueService service;

    // GET current value (used by dashboard frontend via /dashboard/value)
    @GetMapping("/value")
    public ResponseEntity<AppValue> getCurrentValue() {
        return ResponseEntity.ok(service.getCurrentValue());
    }

    // VIEW ALL
    @GetMapping("/view")
    public Iterable<AppValue> getAllValues() {
        return repo.findAll();
    }

    // VIEW SINGLE - Clean REST endpoint
    @GetMapping("/{id}")
    public ResponseEntity<AppValue> getById(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // VIEW SINGLE - Legacy endpoint (kept for backward compatibility)
    @GetMapping("/view/{id}")
    public ResponseEntity<AppValue> getValueById(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE (PUT) - Logic moved to Service
    @PutMapping("/update/{id}")
    public ResponseEntity<AppValue> updateValuePut(@PathVariable Long id, @RequestBody AppValue details) {
        // Calls service to update. Note: id is effectively ignored as service targets
        // the singleton
        // but we could extend service to support IDs if needed.
        // For now, consistent with requirements for "single dashboard value".
        return ResponseEntity.ok(service.updateValues(details.getVoltage(), details.getCurrent(), details.getEnergy()));
    }

    // DELETE
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteValue(@PathVariable Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Existing UPDATE (POST) - simplified for dashboard direct use
    @PostMapping("/update")
    public ResponseEntity<AppValue> updateValue(
            @RequestParam(required = false) BigDecimal voltage,
            @RequestParam(required = false) BigDecimal current,
            @RequestParam(required = false) BigDecimal power, // Ignored for calculation
            @RequestParam(required = false) BigDecimal energy) {

        return ResponseEntity.ok(service.updateValues(voltage, current, energy));
    }
}
