package com.user.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Around("execution(* com.user.service.impl..*(..))")
    public Object logExecution(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String className = proceedingJoinPoint.getTarget().getClass().getSimpleName();
        String methodName = proceedingJoinPoint.getSignature().getName();
        String signature = className + "." + methodName;
        String timestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now());


        long start = System.currentTimeMillis();
        log.info("➡️ [{}] | Entering {}()", timestamp, signature);

        try {
            Object result = proceedingJoinPoint.proceed();
            long duration = System.currentTimeMillis() - start;
            log.info("➡️ [{}] | Exiting {}() | Duration={}ms", timestamp, signature, duration);
            return result;
        } catch (Throwable ex) {
            long duration = System.currentTimeMillis() - start;
            log.error("➡️ [{}] | Exception in {}() | Duration={}ms | Message: {}",
                    timestamp, signature, duration, ex.getMessage(), ex);
            throw ex;
        }
    }
}
