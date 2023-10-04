package com.antk7894.amatda.service;

import com.antk7894.amatda.dto.task.request.TaskCreateDto;
import com.antk7894.amatda.dto.task.request.TaskUpdateDto;
import com.antk7894.amatda.entity.Task;
import com.antk7894.amatda.entity.planner.Planner;
import com.antk7894.amatda.entity.planner.UserRole;
import com.antk7894.amatda.exception.custom.NoAuthenticationException;
import com.antk7894.amatda.repository.TaskRepository;
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
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private SecurityService securityService;

    @InjectMocks
    private TaskService taskService;

    @Test
    @DisplayName("관리자 - 모든 업무를 조회한다")
    void findAllFromAdmin() {
        // given
        Planner planner = new Planner("email", "password", UserRole.ADMIN);
        Task task = new Task(planner, "title", "desc", false, LocalDateTime.now());
        given(taskRepository.findAll(any(Pageable.class))).willReturn(new PageImpl<>(List.of(task)));
        given(securityService.getCurrentUser()).willReturn(planner);

        // when
        Page<Task> taskPage = taskService.findAll(Pageable.ofSize(10));

        // then
        assertThat(taskPage.getTotalElements()).isEqualTo(1);
        verify(taskRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("사용자 - 모든 업무를 조회한다")
    void findAllFromUser() {
        // given
        Planner planner = new Planner("email", "password", UserRole.USER);
        Task task = new Task(planner, "title", "desc", false, LocalDateTime.now());
        given(taskRepository.findByPlanner(any(Planner.class), any(Pageable.class))).willReturn(new PageImpl<>(List.of(task)));
        given(securityService.getCurrentUser()).willReturn(planner);

        // when
        Page<Task> taskPage = taskService.findAll(Pageable.ofSize(10));

        // then
        assertThat(taskPage.getTotalElements()).isEqualTo(1);
        verify(taskRepository, times(1)).findByPlanner(any(), any());
    }

    @Test
    @DisplayName("ID를 통해 단일 업무를 조회한다")
    void findOneById() {
        // given
        Planner plannerMock = mock(Planner.class);
        given(plannerMock.hasSameId(any())).willReturn(true);
        given(plannerMock.getRole()).willReturn(UserRole.USER);

        Task taskMock = mock(Task.class);
        given(taskMock.getTitle()).willReturn("title");


        given(taskRepository.findById(any(Long.class))).willReturn(Optional.of(taskMock));
        given(securityService.getCurrentUser()).willReturn(plannerMock);

        // when
        Task founded = taskService.findOneById(1L);

        // then
        assertThat(founded.getTitle()).isEqualTo("title");
        verify(taskRepository, times(1)).findById(any());
    }

    @Test
    @DisplayName("단일 업무를 저장한다")
    void saveOne() {
        // given
        Task taskMock = mock(Task.class);
        given(taskMock.getTitle()).willReturn("title");
        given(taskRepository.save(any(Task.class))).willReturn(taskMock);

        // when
        Task task = taskService.saveOne(new TaskCreateDto("title", "desc", LocalDateTime.now()));

        // then
        assertThat(task.getTitle()).isEqualTo("title");
        verify(taskRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("단일 업무의 내용을 수정한다")
    void updateOne() {
        // given
        Planner plannerMock = mock(Planner.class);
        given(plannerMock.hasSameId(any())).willReturn(true);
        given(plannerMock.getRole()).willReturn(UserRole.USER);

        Task taskMock = mock(Task.class);

        given(taskRepository.findById(any(Long.class))).willReturn(Optional.of(taskMock));
        given(securityService.getCurrentUser()).willReturn(plannerMock);

        // when
        taskService.updateOne(1L, new TaskUpdateDto("new title", "update task", LocalDateTime.now()));

        // then
        verify(taskRepository, times(1)).findById(any());
        verify(taskMock, times(1)).updateTask(any(), any(), any());
    }

    @Test
    @DisplayName("단일 업무의 상태를 \"수행 완료\"로 수정한다")
    void finishOne() {
        // given
        Planner plannerMock = mock(Planner.class);
        given(plannerMock.hasSameId(any())).willReturn(true);
        given(plannerMock.getRole()).willReturn(UserRole.USER);

        Task taskMock = mock(Task.class);

        given(taskRepository.findById(any(Long.class))).willReturn(Optional.of(taskMock));
        given(securityService.getCurrentUser()).willReturn(plannerMock);

        // when
        taskService.finishOne(1L);

        // then
        verify(taskRepository, times(1)).findById(any());
        verify(taskMock, times(1)).setFinished(true);
    }

    @Test
    @DisplayName("단일 업무의 상태를 \"미수행\"로 수정한다")
    void resetOne() {
        // given
        Planner plannerMock = mock(Planner.class);
        given(plannerMock.hasSameId(any())).willReturn(true);
        given(plannerMock.getRole()).willReturn(UserRole.USER);

        Task taskMock = mock(Task.class);

        given(taskRepository.findById(any(Long.class))).willReturn(Optional.of(taskMock));
        given(securityService.getCurrentUser()).willReturn(plannerMock);

        // when
        taskService.resetOne(1L);

        // then
        verify(taskRepository, times(1)).findById(any());
        verify(taskMock, times(1)).setFinished(false);
    }

    @Test
    @DisplayName("단일 업무를 삭제한다")
    void removeOne() {
        // given
        Planner plannerMock = mock(Planner.class);
        given(plannerMock.hasSameId(any())).willReturn(true);
        given(plannerMock.getRole()).willReturn(UserRole.USER);

        Task taskMock = mock(Task.class);

        given(taskRepository.findById(any(Long.class))).willReturn(Optional.of(taskMock));
        given(securityService.getCurrentUser()).willReturn(plannerMock);

        // when
        taskService.removeOne(1L);

        // then
        verify(taskRepository, times(1)).findById(any());
        verify(taskRepository, times(1)).delete(any());
    }

    @Test
    @DisplayName("권한히 없는 업무에 접근할 경우 NoAuthenticationException 예외를 발생시킨다")
    void hasNoAuthentication() {
        // given
        Planner plannerMock = mock(Planner.class);
        given(plannerMock.hasSameId(any())).willReturn(false);
        given(plannerMock.getRole()).willReturn(UserRole.USER);

        Task taskMock = mock(Task.class);

        given(taskRepository.findById(any(Long.class))).willReturn(Optional.of(taskMock));
        given(securityService.getCurrentUser()).willReturn(plannerMock);

        // when & then
        assertThatThrownBy(() -> taskService.findOneById(1L))
                .isExactlyInstanceOf(NoAuthenticationException.class);
    }

}