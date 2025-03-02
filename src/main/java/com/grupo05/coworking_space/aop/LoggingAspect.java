package com.grupo05.coworking_space.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @Aspect para indicar que es un Aspecto de la aplicación.
 * @Component para indicar que es un componente de Spring.
 * @Slf4j para habilitar el uso de logs en la aplicación.
 */
@Aspect
@Component
@Slf4j
public class LoggingAspect {

    /** Define el paquete base de la aplicación. 
     * @Pointcut para definir un punto de corte.
    */
    @Pointcut("execution(* com.grupo05.coworking.service.*.*(..))")
    public void applicationMethods() {
    }

    /** 
     * Imprime el nombre del método antes de ejecutarse.
     * @param joinPoint punto de unión del método a ejecutar.
     * @Before para indicar que el método se ejecutará antes de la ejecución del método.
     */ 
    @Before("applicationPackagePointcut()")
    public void logBeforeMethodExecution(JoinPoint joinPoint) {
        log.info("Ejecutando método: {}", joinPoint.getSignature().toShortString());
    }

    /** 
     * Mide el tiempo de ejecución del método y lo imprime.
     * @param joinPoint punto de unión del método a ejecutar.
     * @return objeto del resultado del método joinPoint.proceed().
     * @throws Throwable excepción lanzada por el método.
     * @Around para indicar que el método se ejecutará alrededor de la ejecución del método.
     */
    @Around("execution(* com.grupo05.coworking.service.*.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long duration = System.currentTimeMillis() - start;

        log.info("Método {} ejecutado en {} ms", joinPoint.getSignature(), duration);
        return result;
    }

    /**
     * Imprime el resultado del método después de ejecutarse.
     * @param joinPoint punto de unión del método a ejecutar.
     * @param result objeto del resultado del método.
     * @AfterReturning para indicar que el método se ejecutará después de la ejecución del método.
     */
    @AfterReturning(pointcut = "applicationMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("Método {} devolvió: {}", joinPoint.getSignature(), result);
    }
}