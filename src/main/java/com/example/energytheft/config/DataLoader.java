package com.example.energytheft.config;

import com.example.energytheft.entity.AppValue;
import com.example.energytheft.entity.EnergyReading;
import com.example.energytheft.entity.TheftAlert;
import com.example.energytheft.repository.AppValueRepository;
import com.example.energytheft.repository.EnergyReadingRepository;
import com.example.energytheft.repository.TheftAlertRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Loads fresh sample data on every application startup.
 * Clears existing data to ensure clean state for demo/testing.
 */
@Component
public class DataLoader implements CommandLineRunner {

        private final EnergyReadingRepository repo;
        private final TheftAlertRepository alertRepo;
        private final AppValueRepository appValueRepository;

        public DataLoader(EnergyReadingRepository repo, TheftAlertRepository alertRepo,
                        AppValueRepository appValueRepository) {
                this.repo = repo;
                this.alertRepo = alertRepo;
                this.appValueRepository = appValueRepository;
        }

        @Override
        public void run(String... args) {
                // Initialize AppValue for dashboard display
                appValueRepository.deleteAll();
                appValueRepository.save(new AppValue(
                                BigDecimal.valueOf(230.0), BigDecimal.valueOf(10.0),
                                BigDecimal.valueOf(2300.0), BigDecimal.valueOf(5.5)));
                System.out.println(
                                "=== DataLoader: AppValue initialized (voltage=230V, current=10A, power=2300W, energy=5.5kWh) ===");

                repo.deleteAll();

                // Normal readings
                repo.save(new EnergyReading(
                                230.0, 5.0, 1150.0, false, "MTR001", LocalDateTime.now().minusHours(5)));
                repo.save(new EnergyReading(
                                228.0, 5.2, 1185.6, false, "MTR001", LocalDateTime.now().minusHours(4)));
                repo.save(new EnergyReading(
                                232.0, 4.8, 1113.6, false, "MTR002", LocalDateTime.now().minusHours(4)));
                repo.save(new EnergyReading(
                                229.0, 5.1, 1167.9, false, "MTR001", LocalDateTime.now().minusHours(3)));
                repo.save(new EnergyReading(
                                231.0, 4.9, 1131.9, false, "MTR002", LocalDateTime.now().minusHours(3)));
                repo.save(new EnergyReading(
                                230.5, 5.0, 1152.5, false, "MTR003", LocalDateTime.now().minusHours(2)));

                // Theft detected readings (low voltage, high current)
                repo.save(new EnergyReading(
                                180.0, 15.0, 2700.0, true, "MTR002", LocalDateTime.now().minusHours(2)));
                repo.save(new EnergyReading(
                                175.0, 18.0, 3150.0, true, "MTR004", LocalDateTime.now().minusHours(1)));

                // Recent normal readings
                repo.save(new EnergyReading(
                                230.0, 5.5, 1265.0, false, "MTR001", LocalDateTime.now().minusMinutes(30)));
                repo.save(new EnergyReading(
                                229.0, 5.3, 1213.7, false, "MTR003", LocalDateTime.now().minusMinutes(15)));
                repo.save(new EnergyReading(
                                231.0, 5.2, 1201.2, false, "MTR001", LocalDateTime.now()));

                System.out.println("=== DataLoader: Sample energy readings inserted (12 total, 2 theft) ===");

                // Create sample theft alerts
                alertRepo.deleteAll();

                TheftAlert alert1 = new TheftAlert("MTR002", "VOLTAGE_ANOMALY",
                                "Low voltage detected: 180V (expected 220-240V). Possible bypass detected.");
                alert1.setTimestamp(LocalDateTime.now().minusHours(2));
                alertRepo.save(alert1);

                TheftAlert alert2 = new TheftAlert("MTR004", "CURRENT_ANOMALY",
                                "High current detected: 18A (normal: 5-10A). Unusual load pattern.");
                alert2.setTimestamp(LocalDateTime.now().minusHours(1));
                alertRepo.save(alert2);

                System.out.println("=== DataLoader: Sample theft alerts inserted (2 alerts) ===");
        }
}
