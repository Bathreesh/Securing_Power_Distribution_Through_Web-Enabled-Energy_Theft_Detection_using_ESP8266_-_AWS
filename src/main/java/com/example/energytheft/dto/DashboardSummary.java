package com.example.energytheft.dto;

/**
 * DTO for dashboard summary statistics
 */
public class DashboardSummary {
    private long totalReadings;
    private long alertCount;
    private double averageVoltage;
    private double averageCurrent;
    private double averagePower;
    private long theftDetectedCount;

    public DashboardSummary() {
    }

    public long getTotalReadings() {
        return totalReadings;
    }

    public void setTotalReadings(long totalReadings) {
        this.totalReadings = totalReadings;
    }

    public long getAlertCount() {
        return alertCount;
    }

    public void setAlertCount(long alertCount) {
        this.alertCount = alertCount;
    }

    public double getAverageVoltage() {
        return averageVoltage;
    }

    public void setAverageVoltage(double averageVoltage) {
        this.averageVoltage = averageVoltage;
    }

    public double getAverageCurrent() {
        return averageCurrent;
    }

    public void setAverageCurrent(double averageCurrent) {
        this.averageCurrent = averageCurrent;
    }

    public double getAveragePower() {
        return averagePower;
    }

    public void setAveragePower(double averagePower) {
        this.averagePower = averagePower;
    }

    public long getTheftDetectedCount() {
        return theftDetectedCount;
    }

    public void setTheftDetectedCount(long theftDetectedCount) {
        this.theftDetectedCount = theftDetectedCount;
    }
}
