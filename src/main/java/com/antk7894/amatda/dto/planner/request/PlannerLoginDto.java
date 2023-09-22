package com.antk7894.amatda.dto.planner.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record PlannerLoginDto(
        @Email
        String email,
        @NotNull
        String password
) {}
