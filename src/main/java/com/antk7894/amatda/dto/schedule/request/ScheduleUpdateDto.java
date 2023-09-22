package com.antk7894.amatda.dto.schedule.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ScheduleUpdateDto(
        @NotNull
        String title,
        String description,
        @NotNull
        LocalDateTime eventDate
) {
}
