package com.antk7894.amatda.controller;

import com.antk7894.amatda.dto.task.request.TaskActionDto;
import com.antk7894.amatda.dto.task.request.TaskCreateDto;
import com.antk7894.amatda.dto.task.request.TaskUpdateDto;
import com.antk7894.amatda.entity.Task;
import com.antk7894.amatda.service.TaskService;
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
    public ResponseEntity<Page<Task>> all(Pageable pageable) {
        return ResponseEntity.ok(taskService.findAll(pageable));
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<Task> one(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskService.findOneById(taskId));
    }

    @PostMapping
    public ResponseEntity<Task> newTask(@RequestBody TaskCreateDto dto) {
        Task task = taskService.saveOne(dto);
        String currentUri = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();
        return ResponseEntity.created(URI.create(currentUri + "/" + task.getTaskId())).body(task);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable Long taskId, @RequestBody TaskUpdateDto dto) {
        return ResponseEntity.ok(taskService.updateOne(taskId, dto));
    }

    @PatchMapping("/{taskId}")
    public ResponseEntity<Task> manipulateTask(@PathVariable Long taskId, @RequestBody TaskActionDto dto) {
        return switch (dto.action()) {
            case FINISH -> ResponseEntity.ok(taskService.finishOne(taskId));
            case RESET -> ResponseEntity.ok(taskService.resetOne(taskId));
        };
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable Long taskId) {
        taskService.removeOne(taskId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
