package com.antk7894.amatda.service;

import com.antk7894.amatda.entity.Daily;
import com.antk7894.amatda.entity.planner.Planner;
import com.antk7894.amatda.entity.planner.UserRole;
import com.antk7894.amatda.repository.DailyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DailyReadService {

    private final DailyRepository dailyRepository;

    public Page<Daily> findAll(Planner planner, Pageable pageable) {
        return switch (planner.getRole()) {
            case ADMIN -> dailyRepository.findAll(pageable);
            case USER -> dailyRepository.findByPlanner(planner, pageable);
        };
    }

    public Daily findOneById(Planner planner, Long dailyId) {
        Daily daily = dailyRepository.findById(dailyId).orElseThrow();
        checkAuthForRead(planner, daily);
        return dailyRepository.findById(dailyId).orElseThrow();
    }

    private void checkAuthForRead(Planner planner, Daily daily) {
        if (!planner.getRole().equals(UserRole.ADMIN) && !planner.hasSameId(daily.getPlanner())) {
            throw new RuntimeException("수정할 권한 없음");
            // TODO 글로벌 예외 처리
        }
    }

}
