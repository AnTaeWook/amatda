package com.antk7894.amatda.dto.task.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record TaskCreateDto(
        @NotNull
        String title,
        String description,
        @NotNull
        LocalDateTime dueDate
) {
}
