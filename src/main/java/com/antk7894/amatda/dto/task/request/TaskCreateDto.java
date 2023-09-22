package com.antk7894.amatda.dto.task.request;

import java.time.LocalDateTime;

public record TaskCreateDto(
        String title,
        String description,
        LocalDateTime dueDate
) {
}
