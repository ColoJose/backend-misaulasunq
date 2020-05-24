package com.misaulasunq.utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Iterator;

import static java.util.Arrays.asList;

@Aspect
@Component
public class ServiceLogger {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Around("execution(public * com.misaulasunq.service.*.*(..)))")
    public Object LogEntryAndArguementsAnnotation(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        String arguments = this.getArgumentsOfCall(proceedingJoinPoint);

        LOGGER.info(
                "{} of {} was Called at {} with arguments: \n {}",
                 proceedingJoinPoint.getClass().toString(),
                proceedingJoinPoint.getTarget().getClass().getSimpleName(),
                LocalDateTime.now().toString(),
                arguments);
        return proceedingJoinPoint.proceed();
    }

    private String getArgumentsOfCall(ProceedingJoinPoint proceedingJoinPoint) {
        StringBuilder argumentsToBuild = new StringBuilder();

        Iterator argumentIterator = asList(proceedingJoinPoint.getArgs()).iterator();
        while(argumentIterator.hasNext()){
            argumentsToBuild.append(argumentIterator.next());

            if(argumentIterator.hasNext()){
                argumentsToBuild.append(", ");
            } else {
                argumentsToBuild.append(".");
            }
        }

        return argumentsToBuild.toString();
    }

}
