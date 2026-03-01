package com.example.energytheft.repository;

import com.example.energytheft.entity.TheftAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TheftAlertRepository extends JpaRepository<TheftAlert, Long> {
    List<TheftAlert> findByResolved(boolean resolved);

    List<TheftAlert> findByMeterId(String meterId);

    List<TheftAlert> findAllByOrderByTimestampDesc();
}
