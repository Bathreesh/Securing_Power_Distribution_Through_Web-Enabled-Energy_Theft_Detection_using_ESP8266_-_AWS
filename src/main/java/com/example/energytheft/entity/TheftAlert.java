package com.example.energytheft.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "THEFT_ALERTS")
public class TheftAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String meterId;

    private String alertType;

    private String description;

    private LocalDateTime timestamp;

    private boolean resolved;

    public TheftAlert() {
        this.timestamp = LocalDateTime.now();
        this.resolved = false;
    }

    public TheftAlert(String meterId, String alertType, String description) {
        this.meterId = meterId;
        this.alertType = alertType;
        this.description = description;
        this.timestamp = LocalDateTime.now();
        this.resolved = false;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMeterId() {
        return meterId;
    }

    public void setMeterId(String meterId) {
        this.meterId = meterId;
    }

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }
}
