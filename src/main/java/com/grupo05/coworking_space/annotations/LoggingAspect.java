package com.grupo05.coworking_space.annotations;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    // Define el paquete base de la aplicación.
    @Pointcut("execution(* com.grupo05.coworking.service.*.*(..))")
    public void applicationMethods() {
    }

    // Imprime el nombre del método antes de ejecutarse.
    @Before("applicationPackagePointcut()")
    public void logBeforeMethodExecution(JoinPoint joinPoint) {
        log.info("Ejecutando método: {}", joinPoint.getSignature().toShortString());
    }

    // Mide el tiempo de ejecución del método y lo imprime.
    @Around("execution(* com.grupo05.coworking.service.*.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long duration = System.currentTimeMillis() - start;

        log.info("Método {} ejecutado en {} ms", joinPoint.getSignature(), duration);
        return result;
    }

    // Imprime el resultado del método después de ejecutarse.
    @AfterReturning(pointcut = "applicationMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("Método {} devolvió: {}", joinPoint.getSignature(), result);
    }
}