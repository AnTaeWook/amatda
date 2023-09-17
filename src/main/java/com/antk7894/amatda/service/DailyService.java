package com.antk7894.amatda.service;

import com.antk7894.amatda.dto.daily.DailyCreateRequestDto;
import com.antk7894.amatda.dto.daily.DailyUpdateRequestDto;
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
@Transactional
@RequiredArgsConstructor
public class DailyService {

    private final DailyRepository dailyRepository;

    @Transactional(readOnly = true)
    public Page<Daily> findAll(Planner planner, Pageable pageable) {
        return switch (planner.getRole()) {
            case ADMIN -> dailyRepository.findAll(pageable);
            case USER -> dailyRepository.findByPlanner(planner, pageable);
        };
    }

    @Transactional(readOnly = true)
    public Daily findOneById(Planner planner, Long dailyId) {
        Daily daily = dailyRepository.findById(dailyId).orElseThrow();
        checkAuth(planner, daily);
        return dailyRepository.findById(dailyId).orElseThrow();
    }

    public Daily saveOne(Planner planner, DailyCreateRequestDto dto) {
        Daily daily = new Daily(planner, dto.title(), dto.description(), false);
        return dailyRepository.save(daily);
    }

    public Daily updateOne(Planner planner, Long dailyId, DailyUpdateRequestDto dto) {
        Daily daily = dailyRepository.findById(dailyId).orElseThrow();
        checkAuth(planner, daily);
        daily.updateTitleAndDescription(dto.title(), dto.description());
        return daily;
    }

    public Daily finishOne(Planner planner, Long dailyId) {
        Daily daily = dailyRepository.findById(dailyId).orElseThrow();
        checkAuth(planner, daily);
        daily.setFinished(true);
        return daily;
    }

    public Daily resetOne(Planner planner, Long dailyId) {
        Daily daily = dailyRepository.findById(dailyId).orElseThrow();
        checkAuth(planner, daily);
        daily.setFinished(false);
        return daily;
    }

    public void removeOne(Planner planner, Long dailyId) {
        Daily daily = dailyRepository.findById(dailyId).orElseThrow();
        checkAuth(planner, daily);
        dailyRepository.delete(daily);
    }

    private void checkAuth(Planner planner, Daily daily) {
        if (!planner.getRole().equals(UserRole.ADMIN) && !planner.hasSameId(daily.getPlanner())) {
            throw new RuntimeException("권한 없음");
            // TODO 글로벌 예외 처리
        }
    }

}
