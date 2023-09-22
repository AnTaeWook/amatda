package com.antk7894.amatda.service;

import com.antk7894.amatda.dto.task.request.TaskCreateDto;
import com.antk7894.amatda.dto.task.request.TaskUpdateDto;
import com.antk7894.amatda.entity.Task;
import com.antk7894.amatda.entity.planner.Planner;
import com.antk7894.amatda.entity.planner.UserRole;
import com.antk7894.amatda.repository.TaskRepository;
import com.antk7894.amatda.util.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final SecurityService securityService;

    @Transactional(readOnly = true)
    public Page<Task> findAll(Pageable pageable) {
        Planner planner = securityService.getCurrentUser();
        return switch (planner.getRole()) {
            case ADMIN -> taskRepository.findAll(pageable);
            case USER -> taskRepository.findByPlanner(planner, pageable);
        };
    }

    @Transactional(readOnly = true)
    public Task findOneById(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow();
        checkAuth(task);
        return task;
    }

    public Task saveOne(TaskCreateDto dto) {
        Planner planner = securityService.getCurrentUser();
        Task task = new Task(planner, dto.title(), dto.description(), false, dto.dueDate());
        return taskRepository.save(task);
    }

    public Task updateOne(Long taskId, TaskUpdateDto dto) {
        Task task = taskRepository.findById(taskId).orElseThrow();
        checkAuth(task);
        task.updateTask(dto.title(), dto.description(), dto.dueDate());
        return task;
    }

    public Task finishOne(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow();
        checkAuth(task);
        task.setFinished(true);
        return task;
    }

    public Task resetOne(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow();
        checkAuth(task);
        task.setFinished(false);
        return task;
    }

    public void removeOne(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow();
        checkAuth(task);
        taskRepository.delete(task);
    }

    private void checkAuth(Task task) {
        Planner planner = securityService.getCurrentUser();
        if (!planner.getRole().equals(UserRole.ADMIN) && !planner.hasSameId(task.getPlanner())) {
            throw new RuntimeException("권한 없음");
            // TODO 글로벌 예외 처리
        }
    }

}
