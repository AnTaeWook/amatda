package com.antk7894.amatda.controller;

import com.antk7894.amatda.dto.task.request.TaskActionDto;
import com.antk7894.amatda.dto.task.request.TaskCreateDto;
import com.antk7894.amatda.dto.task.request.TaskUpdateDto;
import com.antk7894.amatda.dto.task.response.TaskInquireDto;
import com.antk7894.amatda.entity.Task;
import com.antk7894.amatda.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<Page<TaskInquireDto>> all(Pageable pageable) {
        return ResponseEntity.ok(taskService.findAll(pageable).map(TaskInquireDto::from));
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskInquireDto> one(@PathVariable Long taskId) {
        return ResponseEntity.ok(TaskInquireDto.from(taskService.findOneById(taskId)));
    }

    @PostMapping
    public ResponseEntity<TaskInquireDto> newTask(@RequestBody @Valid TaskCreateDto dto) {
        Task task = taskService.saveOne(dto);
        String currentUri = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();
        URI taskUri = URI.create(currentUri + "/" + task.getTaskId());
        return ResponseEntity.created(taskUri).body(TaskInquireDto.from(task));
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskInquireDto> updateTask(@PathVariable Long taskId, @RequestBody @Valid TaskUpdateDto dto) {
        return ResponseEntity.ok(TaskInquireDto.from(taskService.updateOne(taskId, dto)));
    }

    @PatchMapping("/{taskId}")
    public ResponseEntity<TaskInquireDto> manipulateTask(@PathVariable Long taskId, @RequestBody @Valid TaskActionDto dto) {
        return switch (dto.action()) {
            case FINISH -> ResponseEntity.ok(TaskInquireDto.from(taskService.finishOne(taskId)));
            case RESET -> ResponseEntity.ok(TaskInquireDto.from(taskService.resetOne(taskId)));
        };
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable Long taskId) {
        taskService.removeOne(taskId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
