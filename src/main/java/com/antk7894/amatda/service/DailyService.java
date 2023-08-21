package com.antk7894.amatda.service;

import com.antk7894.amatda.dto.daily.DailyCreateRequestDto;
import com.antk7894.amatda.dto.daily.DailyUpdateRequestDto;
import com.antk7894.amatda.entity.Daily;
import com.antk7894.amatda.entity.planner.Planner;
import com.antk7894.amatda.entity.planner.UserRole;
import com.antk7894.amatda.repository.DailyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DailyService {

    private final DailyRepository dailyRepository;

    public Daily saveOne(Planner planner, DailyCreateRequestDto dto) {
        Daily daily = new Daily(planner, dto.title(), dto.description(), false);
        return dailyRepository.save(daily);
    }

    @Transactional
    public Daily updateOne(Planner planner, Long dailyId, DailyUpdateRequestDto dto) {
        Daily daily = dailyRepository.findById(dailyId).orElseThrow();
        checkAuthForUpdate(planner, daily);
        daily.updateTitleAndDescription(dto.title(), dto.description());
        return daily;
    }

    @Transactional
    public Daily finishOne(Planner planner, Long dailyId) {
        Daily daily = dailyRepository.findById(dailyId).orElseThrow();
        checkAuthForUpdate(planner, daily);
        daily.setFinished(true);
        return daily;
    }

    @Transactional
    public Daily resetOne(Planner planner, Long dailyId) {
        Daily daily = dailyRepository.findById(dailyId).orElseThrow();
        checkAuthForUpdate(planner, daily);
        daily.setFinished(false);
        return daily;
    }

    @Transactional
    public void removeOne(Planner planner, Long dailyId) {
        Daily daily = dailyRepository.findById(dailyId).orElseThrow();
        checkAuthForUpdate(planner, daily);
        dailyRepository.delete(daily);
    }

    private void checkAuthForUpdate(Planner planner, Daily daily) {
        if (!planner.getRole().equals(UserRole.ADMIN) && !planner.hasSameId(daily.getPlanner())) {
            throw new RuntimeException("수정할 권한 없음");
            // TODO 글로벌 예외 처리
        }
    }

}
