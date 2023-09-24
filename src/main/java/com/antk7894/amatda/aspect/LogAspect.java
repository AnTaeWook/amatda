package com.antk7894.amatda.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LogAspect {

    @Pointcut("within(com.antk7894.amatda.controller.*)")
    private void controllerPoint() {}

    @Before("controllerPoint()")
    public void logRequestDto(JoinPoint joinPoint) {
        log.info("[Request] method:{} body:{}", joinPoint.getSignature().toShortString(), joinPoint.getArgs());
    }

    @AfterReturning(pointcut = "controllerPoint()", returning = "returnValue")
    public void logResponseDto(JoinPoint joinPoint, Object returnValue) {
        log.info("[Response] method:{} body{}", joinPoint.getSignature().toShortString(), returnValue);
    }

    @AfterThrowing(pointcut = "controllerPoint()", throwing = "exception")
    public void logException(JoinPoint joinPoint, Exception exception) {
        log.error("[Error] method:{} message:{}", joinPoint.getSignature().toShortString(), exception.getMessage());
    }

}
