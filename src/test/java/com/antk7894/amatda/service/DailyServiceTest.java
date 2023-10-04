package com.antk7894.amatda.service;

import com.antk7894.amatda.dto.daily.request.DailyCreateDto;
import com.antk7894.amatda.dto.daily.request.DailyUpdateDto;
import com.antk7894.amatda.entity.Daily;
import com.antk7894.amatda.entity.planner.Planner;
import com.antk7894.amatda.entity.planner.UserRole;
import com.antk7894.amatda.exception.custom.NoAuthenticationException;
import com.antk7894.amatda.repository.DailyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DailyServiceTest {

    @Mock
    private DailyRepository dailyRepository;

    @Mock
    private SecurityService securityService;

    @InjectMocks
    private DailyService dailyService;

    @Test
    @DisplayName("관리자 - 모든 일일 작업을 조회한다")
    void findAllFromAdmin() {
        // given
        Planner planner = new Planner("email", "password", UserRole.ADMIN);
        Daily daily = new Daily(planner, "title", "desc", false);
        given(dailyRepository.findAll(any(Pageable.class))).willReturn(new PageImpl<>(List.of(daily)));
        given(securityService.getCurrentUser()).willReturn(planner);

        // when
        Page<Daily> dailyPage = dailyService.findAll(Pageable.ofSize(10));

        // then
        assertThat(dailyPage.getTotalElements()).isEqualTo(1);
        verify(dailyRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("사용자 - 모든 일일 작업을 조회한다")
    void findAllFromUser() {
        // given
        Planner planner = new Planner("email", "password", UserRole.USER);
        Daily daily = new Daily(planner, "title", "desc", false);
        given(dailyRepository.findByPlanner(any(Planner.class), any(Pageable.class))).willReturn(new PageImpl<>(List.of(daily)));
        given(securityService.getCurrentUser()).willReturn(planner);

        // when
        Page<Daily> dailyPage = dailyService.findAll(Pageable.ofSize(10));

        // then
        assertThat(dailyPage.getTotalElements()).isEqualTo(1);
        verify(dailyRepository, times(1)).findByPlanner(any(), any());
    }

    @Test
    @DisplayName("ID를 통해 단일 일일 작업을 조회한다")
    void findOneById() {
        // given
        Planner plannerMock = mock(Planner.class);
        given(plannerMock.hasSameId(any())).willReturn(true);
        given(plannerMock.getRole()).willReturn(UserRole.USER);

        Daily dailyMock = mock(Daily.class);
        given(dailyMock.getTitle()).willReturn("title");


        given(dailyRepository.findById(any(Long.class))).willReturn(Optional.of(dailyMock));
        given(securityService.getCurrentUser()).willReturn(plannerMock);

        // when
        Daily founded = dailyService.findOneById(1L);

        // then
        assertThat(founded.getTitle()).isEqualTo("title");
        verify(dailyRepository, times(1)).findById(any());
    }

    @Test
    @DisplayName("단일 일일 작업을 저장한다")
    void saveOne() {
        // given
        Daily dailyMock = mock(Daily.class);
        given(dailyMock.getTitle()).willReturn("title");
        given(dailyRepository.save(any(Daily.class))).willReturn(dailyMock);

        // when
        Daily daily = dailyService.saveOne(new DailyCreateDto("title", "desc"));

        // then
        assertThat(daily.getTitle()).isEqualTo("title");
        verify(dailyRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("단일 일일 작업의 내용을 수정한다")
    void updateOne() {
        // given
        Planner plannerMock = mock(Planner.class);
        given(plannerMock.hasSameId(any())).willReturn(true);
        given(plannerMock.getRole()).willReturn(UserRole.USER);

        Daily dailyMock = mock(Daily.class);

        given(dailyRepository.findById(any(Long.class))).willReturn(Optional.of(dailyMock));
        given(securityService.getCurrentUser()).willReturn(plannerMock);

        // when
        dailyService.updateOne(1L, new DailyUpdateDto("new title", "update daily"));

        // then
        verify(dailyRepository, times(1)).findById(any());
        verify(dailyMock, times(1)).updateTitleAndDescription(any(), any());
    }

    @Test
    @DisplayName("단일 일일 작업의 상태를 \"수행 완료\"로 수정한다")
    void finishOne() {
        // given
        Planner plannerMock = mock(Planner.class);
        given(plannerMock.hasSameId(any())).willReturn(true);
        given(plannerMock.getRole()).willReturn(UserRole.USER);

        Daily dailyMock = mock(Daily.class);

        given(dailyRepository.findById(any(Long.class))).willReturn(Optional.of(dailyMock));
        given(securityService.getCurrentUser()).willReturn(plannerMock);

        // when
        dailyService.finishOne(1L);

        // then
        verify(dailyRepository, times(1)).findById(any());
        verify(dailyMock, times(1)).setFinished(true);
    }

    @Test
    @DisplayName("단일 일일 작업의 상태를 \"미수행\"로 수정한다")
    void resetOne() {
        // given
        Planner plannerMock = mock(Planner.class);
        given(plannerMock.hasSameId(any())).willReturn(true);
        given(plannerMock.getRole()).willReturn(UserRole.USER);

        Daily dailyMock = mock(Daily.class);

        given(dailyRepository.findById(any(Long.class))).willReturn(Optional.of(dailyMock));
        given(securityService.getCurrentUser()).willReturn(plannerMock);

        // when
        dailyService.resetOne(1L);

        // then
        verify(dailyRepository, times(1)).findById(any());
        verify(dailyMock, times(1)).setFinished(false);
    }

    @Test
    @DisplayName("단일 일일 작업을 삭제한다")
    void removeOne() {
        // given
        Planner plannerMock = mock(Planner.class);
        given(plannerMock.hasSameId(any())).willReturn(true);
        given(plannerMock.getRole()).willReturn(UserRole.USER);

        Daily dailyMock = mock(Daily.class);

        given(dailyRepository.findById(any(Long.class))).willReturn(Optional.of(dailyMock));
        given(securityService.getCurrentUser()).willReturn(plannerMock);

        // when
        dailyService.removeOne(1L);

        // then
        verify(dailyRepository, times(1)).findById(any());
        verify(dailyRepository, times(1)).delete(any());
    }

    @Test
    @DisplayName("권한히 없는 일일 작업에 접근할 경우 NoAuthenticationException 예외를 발생시킨다")
    void hasNoAuthentication() {
        // given
        Planner plannerMock = mock(Planner.class);
        given(plannerMock.hasSameId(any())).willReturn(false);
        given(plannerMock.getRole()).willReturn(UserRole.USER);

        Daily dailyMock = mock(Daily.class);

        given(dailyRepository.findById(any(Long.class))).willReturn(Optional.of(dailyMock));
        given(securityService.getCurrentUser()).willReturn(plannerMock);

        // when & then
        assertThatThrownBy(() -> dailyService.findOneById(1L))
                .isExactlyInstanceOf(NoAuthenticationException.class);
    }

}