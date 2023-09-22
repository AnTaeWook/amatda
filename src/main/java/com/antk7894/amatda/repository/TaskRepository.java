package com.antk7894.amatda.repository;

import com.antk7894.amatda.entity.Task;
import com.antk7894.amatda.entity.planner.Planner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByPlanner(Planner planner, Pageable pageable);

}
