package com.antk7894.amatda.dto.task.request;

import com.antk7894.amatda.dto.task.TaskAction;
import jakarta.validation.constraints.NotNull;

public record TaskActionDto(
        @NotNull
        TaskAction action
) {
}
