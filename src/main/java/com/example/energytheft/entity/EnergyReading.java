package com.example.energytheft.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ENERGY_READINGS")
public class EnergyReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double voltage;
    private double current;
    private double power;

    @Column(name = "THEFT_DETECTED")
    private boolean theftDetected;

    private String meterId;

    private LocalDateTime timestamp;

    public EnergyReading() {
        this.timestamp = LocalDateTime.now();
    }

    public EnergyReading(double voltage, double current, double power, boolean theftDetected, String meterId,
            LocalDateTime timestamp) {
        this.voltage = voltage;
        this.current = current;
        this.power = power;
        this.theftDetected = theftDetected;
        this.meterId = meterId;
        this.timestamp = timestamp;
    }

    // Legacy constructor for backward compatibility
    public EnergyReading(String meterId, double voltage, double current, double power, boolean theftDetected) {
        this.meterId = meterId;
        this.voltage = voltage;
        this.current = current;
        this.power = power;
        this.theftDetected = theftDetected;
        this.timestamp = LocalDateTime.now();
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

    public double getVoltage() {
        return voltage;
    }

    public void setVoltage(double voltage) {
        this.voltage = voltage;
    }

    public double getCurrent() {
        return current;
    }

    public void setCurrent(double current) {
        this.current = current;
    }

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public boolean isTheftDetected() {
        return theftDetected;
    }

    public void setTheftDetected(boolean theftDetected) {
        this.theftDetected = theftDetected;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
