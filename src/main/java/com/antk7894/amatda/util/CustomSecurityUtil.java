package com.antk7894.amatda.util;

import com.antk7894.amatda.entity.planner.Planner;
import com.antk7894.amatda.service.PlannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CustomSecurityUtil {

    private final PlannerService plannerService;

    public Planner getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication) || Objects.isNull(authentication.getName())) {
            throw new RuntimeException();
        }
        return plannerService.findOneByEmail(authentication.getName());
    }

}
