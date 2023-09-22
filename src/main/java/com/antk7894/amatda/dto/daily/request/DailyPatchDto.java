package com.antk7894.amatda.dto.daily.request;

import com.antk7894.amatda.dto.daily.DailyAction;

public record DailyPatchDto(
        DailyAction action
) {
}