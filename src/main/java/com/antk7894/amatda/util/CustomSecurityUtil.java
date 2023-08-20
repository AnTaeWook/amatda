package com.antk7894.amatda.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

public class CustomSecurityUtil {

    public static String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication) || Objects.isNull(authentication.getName())) {
            throw new RuntimeException();
        }
        return authentication.getName();
    }

}
