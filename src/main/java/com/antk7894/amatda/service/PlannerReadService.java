package com.antk7894.amatda.service;

import com.antk7894.amatda.entity.planner.Planner;
import com.antk7894.amatda.repository.PlannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlannerReadService {

    private final PlannerRepository plannerRepository;

    public Page<Planner> findAll(Pageable pageable) {
        return plannerRepository.findAll(pageable);
    }

    public Planner findOneById(Long plannerId) {
        return plannerRepository.findById(plannerId).orElseThrow();
    }

    public Planner findOneByEmail(String email) {
        return plannerRepository.findByEmail(email).orElseThrow();
    }

}
