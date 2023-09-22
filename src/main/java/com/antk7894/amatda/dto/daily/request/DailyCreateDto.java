package com.antk7894.amatda.dto.daily.request;

import jakarta.validation.constraints.NotNull;

public record DailyCreateDto(
        @NotNull
        String title,
        String description
) {
}
