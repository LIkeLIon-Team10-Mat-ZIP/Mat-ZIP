package site.matzip.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(* site.matzip.*.controller.*Controller.*(..))")
    private void loggingTarget() {

    }

    @Before("loggingTarget()")
    public void logRequest(final JoinPoint joinPoint) {
        CodeSignature signature = (CodeSignature) joinPoint.getSignature();

        log.info("[Request] Controller: {} / Method: {} / Arguments: {}",
                joinPoint.getTarget().getClass().getSimpleName(),
                signature.getName(),
                toString(signature.getParameterNames(), joinPoint.getArgs())
        );
    }

    private String toString(final String[] parameterNames, final Object[] args) {
        return IntStream.range(0, parameterNames.length)
                .mapToObj(i -> parameterNames[i] + "=" + args[i])
                .collect(Collectors.joining(","));
    }
}
