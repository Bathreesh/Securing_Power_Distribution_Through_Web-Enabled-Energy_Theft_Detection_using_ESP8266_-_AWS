package com.example.energytheft.service;

import com.example.energytheft.entity.AppValue;
import com.example.energytheft.repository.AppValueRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class AppValueService {

    @Autowired
    private AppValueRepository appValueRepository;

    private static final Long DEFAULT_ID = 1L;
    private static final int SCALE = 6;
    private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;

    public AppValue getCurrentValue() {
        return appValueRepository.findById(DEFAULT_ID)
                .orElseGet(this::createDefaultValue);
    }

    private AppValue createDefaultValue() {
        AppValue v = new AppValue(
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO);
        v.setId(DEFAULT_ID);
        v.setLastUpdated(LocalDateTime.now());
        return appValueRepository.save(v);
    }

    @Transactional
    public AppValue updateValues(BigDecimal voltage, BigDecimal current, BigDecimal manualEnergy) {
        AppValue appValue = appValueRepository.findById(DEFAULT_ID)
                .orElse(createDefaultValue());

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastUpdated = appValue.getLastUpdated();

        // 1. Calculate Time Difference (in seconds)
        long secondsDiff = 0;
        if (lastUpdated != null) {
            secondsDiff = ChronoUnit.SECONDS.between(lastUpdated, now);
        }

        // 2. Calculate Energy Increment based on OLD Power (Integration)
        // Energy (kWh) += Power (W) * Time (h) / 1000
        // Time (h) = secondsDiff / 3600

        BigDecimal oldPower = appValue.getPower() != null ? appValue.getPower() : BigDecimal.ZERO;

        // Avoid calculation if time diff is negligible or negative
        if (secondsDiff > 0) {
            BigDecimal timeInHours = BigDecimal.valueOf(secondsDiff)
                    .divide(BigDecimal.valueOf(3600), SCALE, ROUNDING);

            BigDecimal energyIncrementKWh = oldPower
                    .multiply(timeInHours)
                    .divide(BigDecimal.valueOf(1000), SCALE, ROUNDING);

            BigDecimal currentEnergy = appValue.getEnergy() != null ? appValue.getEnergy() : BigDecimal.ZERO;
            appValue.setEnergy(currentEnergy.add(energyIncrementKWh));
        }

        // 3. Update Voltage and Current if provided
        if (voltage != null) {
            appValue.setVoltage(voltage);
        }
        if (current != null) {
            appValue.setCurrent(current);
        }

        // 4. Calculate NEW Power = Voltage * Current (W)
        BigDecimal currentVolts = appValue.getVoltage() != null ? appValue.getVoltage() : BigDecimal.ZERO;
        BigDecimal currentAmps = appValue.getCurrent() != null ? appValue.getCurrent() : BigDecimal.ZERO;
        BigDecimal newPower = currentVolts.multiply(currentAmps);
        appValue.setPower(newPower);

        // 5. Override Energy if manual value provided
        if (manualEnergy != null) {
            appValue.setEnergy(manualEnergy);
        }

        // 6. Update Timestamp
        appValue.setLastUpdated(now);

        return appValueRepository.save(appValue);
    }
}
