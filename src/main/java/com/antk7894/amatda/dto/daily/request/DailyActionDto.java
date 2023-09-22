package com.antk7894.amatda.dto.daily.request;

import com.antk7894.amatda.dto.daily.DailyAction;
import jakarta.validation.constraints.NotNull;

public record DailyActionDto(
        @NotNull
        DailyAction action
) {
}