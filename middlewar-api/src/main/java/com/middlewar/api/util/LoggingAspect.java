package com.middlewar.api.util;

import com.middlewar.api.annotations.Dev;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Dev
@Slf4j
public class LoggingAspect {

    // Cette méthode est appelée à chaque fois (et avant) qu'une méthode controller soit executée
    @Before("execution(* com.middlewar.api.gameserver.controllers.*.*(..))")
    public void logMethodEntry(JoinPoint joinPoint) {

        Object[] args = joinPoint.getArgs();

        // Nom de la méthode interceptée
        String name = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        StringBuffer sb = new StringBuffer(className + " > " + name + " Begin - called with: [");

        // Liste des valeurs des arguments reçus par la méthode
        for (int i = 0; i < args.length; i++) {
            Object o = args[i];
            sb.append("'" + o + "'");
            sb.append((i == (args.length - 1)) ? "" : ", ");
        }
        sb.append("]");

        log.info(sb.toString());
    }

    @AfterReturning(pointcut = "execution(* com.middlewar.api.gameserver.controllers.*.*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        // Nom de la méthode interceptée
        String name = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        log.info(className + " > " + name + " End - returning: [" + result + "]");
    }
}