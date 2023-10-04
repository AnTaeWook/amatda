package com.antk7894.amatda.service;

import com.antk7894.amatda.entity.planner.Planner;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final PlannerService plannerService;

    public Planner getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication) || Objects.isNull(authentication.getName())) {
            throw new RuntimeException();
        }
        return plannerService.findOneByEmail(authentication.getName());
    }

}
