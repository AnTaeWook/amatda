package com.antk7894.amatda.repository;

import com.antk7894.amatda.entity.planner.Planner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlannerRepository extends JpaRepository<Planner, Long> {
    Optional<Planner> findByEmail(String email);
}
