package com.antk7894.amatda.service;

import com.antk7894.amatda.dto.schedule.request.ScheduleCreateDto;
import com.antk7894.amatda.dto.schedule.request.ScheduleUpdateDto;
import com.antk7894.amatda.entity.Schedule;
import com.antk7894.amatda.entity.planner.Planner;
import com.antk7894.amatda.entity.planner.UserRole;
import com.antk7894.amatda.exception.custom.NoAuthenticationException;
import com.antk7894.amatda.repository.ScheduleRepository;
import com.antk7894.amatda.util.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final SecurityService securityService;

    @Transactional(readOnly = true)
    public Page<Schedule> findAll(Pageable pageable) {
        Planner planner = securityService.getCurrentUser();
        return switch (planner.getRole()) {
            case ADMIN -> scheduleRepository.findAll(pageable);
            case USER -> scheduleRepository.findByPlanner(planner, pageable);
        };
    }

    @Transactional(readOnly = true)
    public Schedule findOneById(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow();
        checkAuth(schedule);
        return schedule;
    }

    public Schedule saveOne(ScheduleCreateDto dto) {
        Planner planner = securityService.getCurrentUser();
        Schedule schedule = new Schedule(planner, dto.title(), dto.description(), dto.eventDate());
        return scheduleRepository.save(schedule);
    }

    public Schedule updateOne(Long scheduleId, ScheduleUpdateDto dto) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow();
        checkAuth(schedule);
        schedule.updateSchedule(dto.title(), dto.description(), dto.eventDate());
        return schedule;
    }

    public void removeOne(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow();
        checkAuth(schedule);
        scheduleRepository.delete(schedule);
    }

    private void checkAuth(Schedule schedule) {
        Planner planner = securityService.getCurrentUser();
        if (!planner.getRole().equals(UserRole.ADMIN) && !planner.hasSameId(schedule.getPlanner())) {
            throw new NoAuthenticationException(planner.getPlannerId(), schedule.getClass().getSimpleName(), schedule.getScheduleId());
        }
    }

}
