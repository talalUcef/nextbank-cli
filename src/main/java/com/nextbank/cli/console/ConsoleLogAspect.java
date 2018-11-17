package com.nextbank.cli.console;

import com.nextbank.cli.domain.AccountOperation;
import com.nextbank.cli.helper.ConsoleHelper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Aspect
@Component
@Profile("!test")
public class ConsoleLogAspect {

    private final ConsoleHelper console;

    public ConsoleLogAspect(ConsoleHelper console) {
        this.console = console;
    }

    @Around("@annotation(com.nextbank.cli.console.ConsoleLog)")
    public void log(ProceedingJoinPoint joinPoint) throws Throwable {
        final Object proceed = joinPoint.proceed();
        if (proceed instanceof List && ((List) proceed).size() > 0) {
            if (((List) proceed).get(0) instanceof String) {
                final List<String> messages = (List<String>) proceed;
                messages.forEach(message -> this.console.write(message));
            } else if (((List) proceed).get(0) instanceof AccountOperation) {
                final List<AccountOperation> journal = (List<AccountOperation>) proceed;
                this.console.write(journal);
            }
        } else {
            final var signature = (MethodSignature) joinPoint.getSignature();
            final Method method = signature.getMethod();
            if ("check".equalsIgnoreCase(method.getName())) {
                this.console.write(new ArrayList<>());
            }
        }
    }
}