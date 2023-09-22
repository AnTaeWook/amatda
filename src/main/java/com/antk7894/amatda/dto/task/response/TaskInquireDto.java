package com.antk7894.amatda.dto.task.response;

import com.antk7894.amatda.entity.Task;

import java.time.LocalDateTime;

public record TaskInquireDto(
        Long taskId,
        String title,
        String description,
        boolean isFinished,
        LocalDateTime dueDate
) {

    public static TaskInquireDto from(Task task) {
        return new TaskInquireDto(task.getTaskId(), task.getTitle(), task.getDescription(),
                task.isFinished(), task.getDueDate()
        );
    }

}
