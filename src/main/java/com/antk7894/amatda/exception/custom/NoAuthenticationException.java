package com.antk7894.amatda.exception.custom;

public class NoAuthenticationException extends RuntimeException {

    public NoAuthenticationException(Long userId, String resourceName, Long resourceId) {
        super(String.format("user id=%d has no authentication about this resource: %s id=%d", userId, resourceName, resourceId));
    }

}
