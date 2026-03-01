package com.example.energytheft.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * REST API Controller for System Status
 * Provides a simple status endpoint for the landing page
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class StatusController {

    /**
     * GET /api/status - Get system online status
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("online", true);
        status.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.ok(status);
    }
}
