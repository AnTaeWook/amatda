package com.antk7894.amatda.dto.planner.response;

import com.antk7894.amatda.entity.planner.Planner;

public record PlannerInquireDto(
        Long plannerId,
        String email,
        String role
) {

    public static PlannerInquireDto from(Planner planner) {
        return new PlannerInquireDto(
                planner.getPlannerId(), planner.getEmail(), planner.getRole().name()
        );
    }

}
