package com.antk7894.amatda.repository;

import com.antk7894.amatda.entity.Daily;
import com.antk7894.amatda.entity.Schedule;
import com.antk7894.amatda.entity.planner.Planner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    Page<Schedule> findByPlanner(Planner planner, Pageable pageable);

}
