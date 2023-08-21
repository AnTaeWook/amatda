package com.antk7894.amatda.repository;

import com.antk7894.amatda.entity.Daily;
import com.antk7894.amatda.entity.planner.Planner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyRepository extends JpaRepository<Daily, Long> {

    Page<Daily> findByPlanner(Planner planner, Pageable pageable);

}
