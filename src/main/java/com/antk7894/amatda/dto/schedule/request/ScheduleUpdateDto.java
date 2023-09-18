package com.antk7894.amatda.dto.schedule.request;

import java.time.LocalDateTime;

public record ScheduleUpdateDto(
        String title,
        String description,
        LocalDateTime eventDate
) {
}
