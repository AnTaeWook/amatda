package com.antk7894.amatda.service;

import com.antk7894.amatda.dto.schedule.request.ScheduleCreateDto;
import com.antk7894.amatda.dto.schedule.request.ScheduleUpdateDto;
import com.antk7894.amatda.dto.schedule.request.ScheduleCreateDto;
import com.antk7894.amatda.dto.schedule.request.ScheduleUpdateDto;
import com.antk7894.amatda.entity.Schedule;
import com.antk7894.amatda.entity.Schedule;
import com.antk7894.amatda.entity.planner.Planner;
import com.antk7894.amatda.entity.planner.UserRole;
import com.antk7894.amatda.exception.custom.NoAuthenticationException;
import com.antk7894.amatda.repository.ScheduleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private SecurityService securityService;

    @InjectMocks
    private ScheduleService scheduleService;

    @Test
    @DisplayName("관리자 - 모든 일정을 조회한다")
    void findAllForAdmin() {
        // given
        Planner planner = new Planner("email", "password", UserRole.ADMIN);
        Schedule schedule = new Schedule(planner, "title", "desc", LocalDateTime.now());
        given(scheduleRepository.findAll(any(Pageable.class))).willReturn(new PageImpl<>(List.of(schedule)));
        given(securityService.getCurrentUser()).willReturn(planner);

        // when
        Page<Schedule> schedulePage = scheduleService.findAll(Pageable.ofSize(10));

        // then
        assertThat(schedulePage.getTotalElements()).isEqualTo(1);
        verify(scheduleRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("사용자 - 모든 일정을 조회한다")
    void findAllForUser() {
        // given
        Planner planner = new Planner("email", "password", UserRole.USER);
        Schedule schedule = new Schedule(planner, "title", "desc", LocalDateTime.now());
        given(scheduleRepository.findByPlanner(any(Planner.class), any(Pageable.class))).willReturn(new PageImpl<>(List.of(schedule)));
        given(securityService.getCurrentUser()).willReturn(planner);

        // when
        Page<Schedule> schedulePage = scheduleService.findAll(Pageable.ofSize(10));

        // then
        assertThat(schedulePage.getTotalElements()).isEqualTo(1);
        verify(scheduleRepository, times(1)).findByPlanner(any(), any());
    }

    @Test
    @DisplayName("ID를 통해 단일 일정을 조회한다")
    void findOneById() {
        // given
        Planner plannerMock = mock(Planner.class);
        given(plannerMock.hasSameId(any())).willReturn(true);
        given(plannerMock.getRole()).willReturn(UserRole.USER);

        Schedule scheduleMock = mock(Schedule.class);
        given(scheduleMock.getTitle()).willReturn("title");


        given(scheduleRepository.findById(any(Long.class))).willReturn(Optional.of(scheduleMock));
        given(securityService.getCurrentUser()).willReturn(plannerMock);

        // when
        Schedule founded = scheduleService.findOneById(1L);

        // then
        assertThat(founded.getTitle()).isEqualTo("title");
        verify(scheduleRepository, times(1)).findById(any());
    }

    @Test
    @DisplayName("단일 일정을 저장한다")
    void saveOne() {
        // given
        Schedule scheduleMock = mock(Schedule.class);
        given(scheduleMock.getTitle()).willReturn("title");
        given(scheduleRepository.save(any(Schedule.class))).willReturn(scheduleMock);

        // when
        Schedule schedule = scheduleService.saveOne(new ScheduleCreateDto("title", "desc", LocalDateTime.now()));

        // then
        assertThat(schedule.getTitle()).isEqualTo("title");
        verify(scheduleRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("단일 일정의 내용을 수정한다")
    void updateOne() {
        // given
        Planner plannerMock = mock(Planner.class);
        given(plannerMock.hasSameId(any())).willReturn(true);
        given(plannerMock.getRole()).willReturn(UserRole.USER);

        Schedule scheduleMock = mock(Schedule.class);

        given(scheduleRepository.findById(any(Long.class))).willReturn(Optional.of(scheduleMock));
        given(securityService.getCurrentUser()).willReturn(plannerMock);

        // when
        scheduleService.updateOne(1L, new ScheduleUpdateDto("new title", "update schedule", LocalDateTime.now()));

        // then
        verify(scheduleRepository, times(1)).findById(any());
        verify(scheduleMock, times(1)).updateSchedule(any(), any(), any());
    }

    @Test
    @DisplayName("단일 일정을 삭제한다")
    void removeOne() {
        // given
        Planner plannerMock = mock(Planner.class);
        given(plannerMock.hasSameId(any())).willReturn(true);
        given(plannerMock.getRole()).willReturn(UserRole.USER);

        Schedule scheduleMock = mock(Schedule.class);

        given(scheduleRepository.findById(any(Long.class))).willReturn(Optional.of(scheduleMock));
        given(securityService.getCurrentUser()).willReturn(plannerMock);

        // when
        scheduleService.removeOne(1L);

        // then
        verify(scheduleRepository, times(1)).findById(any());
        verify(scheduleRepository, times(1)).delete(any());
    }

    @Test
    @DisplayName("권한히 없는 일정에 접근할 경우 NoAuthenticationException 예외를 발생시킨다")
    void hasNoAuthentication() {
        // given
        Planner plannerMock = mock(Planner.class);
        given(plannerMock.hasSameId(any())).willReturn(false);
        given(plannerMock.getRole()).willReturn(UserRole.USER);

        Schedule scheduleMock = mock(Schedule.class);

        given(scheduleRepository.findById(any(Long.class))).willReturn(Optional.of(scheduleMock));
        given(securityService.getCurrentUser()).willReturn(plannerMock);

        // when & then
        assertThatThrownBy(() -> scheduleService.findOneById(1L))
                .isExactlyInstanceOf(NoAuthenticationException.class);
    }
    
}