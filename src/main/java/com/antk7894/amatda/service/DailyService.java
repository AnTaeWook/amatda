package com.antk7894.amatda.service;

import com.antk7894.amatda.dto.daily.DailyCreateRequestDto;
import com.antk7894.amatda.dto.daily.DailyUpdateRequestDto;
import com.antk7894.amatda.entity.Daily;
import com.antk7894.amatda.entity.planner.Planner;
import com.antk7894.amatda.entity.planner.UserRole;
import com.antk7894.amatda.repository.DailyRepository;
import com.antk7894.amatda.util.SecurityService;
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
    private final SecurityService securityService;

    @Transactional(readOnly = true)
    public Page<Daily> findAll(Pageable pageable) {
        Planner planner = securityService.getCurrentUser();
        return switch (planner.getRole()) {
            case ADMIN -> dailyRepository.findAll(pageable);
            case USER -> dailyRepository.findByPlanner(planner, pageable);
        };
    }

    @Transactional(readOnly = true)
    public Daily findOneById(Long dailyId) {
        Daily daily = dailyRepository.findById(dailyId).orElseThrow();
        checkAuth(daily);
        return daily;
    }

    public Daily saveOne(DailyCreateRequestDto dto) {
        Planner planner = securityService.getCurrentUser();
        Daily daily = new Daily(planner, dto.title(), dto.description(), false);
        return dailyRepository.save(daily);
    }

    public Daily updateOne(Long dailyId, DailyUpdateRequestDto dto) {
        Daily daily = dailyRepository.findById(dailyId).orElseThrow();
        checkAuth(daily);
        daily.updateTitleAndDescription(dto.title(), dto.description());
        return daily;
    }

    public Daily finishOne(Long dailyId) {
        Daily daily = dailyRepository.findById(dailyId).orElseThrow();
        checkAuth(daily);
        daily.setFinished(true);
        return daily;
    }

    public Daily resetOne(Long dailyId) {
        Daily daily = dailyRepository.findById(dailyId).orElseThrow();
        checkAuth(daily);
        daily.setFinished(false);
        return daily;
    }

    public void removeOne(Long dailyId) {
        Daily daily = dailyRepository.findById(dailyId).orElseThrow();
        checkAuth(daily);
        dailyRepository.delete(daily);
    }

    private void checkAuth(Daily daily) {
        Planner planner = securityService.getCurrentUser();
        if (!planner.getRole().equals(UserRole.ADMIN) && !planner.hasSameId(daily.getPlanner())) {
            throw new RuntimeException("권한 없음");
            // TODO 글로벌 예외 처리
        }
    }

}
