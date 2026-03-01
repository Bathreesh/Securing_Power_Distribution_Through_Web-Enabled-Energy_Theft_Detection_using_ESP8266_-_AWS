package com.example.energytheft.repository;

import com.example.energytheft.entity.AppValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppValueRepository extends JpaRepository<AppValue, Long> {
}
