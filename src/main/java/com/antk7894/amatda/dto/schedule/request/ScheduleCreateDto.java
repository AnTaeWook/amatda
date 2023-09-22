package com.antk7894.amatda.dto.schedule.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ScheduleCreateDto(
        @NotNull
        String title,
        String description,
        @NotNull
        LocalDateTime eventDate
) {
}
