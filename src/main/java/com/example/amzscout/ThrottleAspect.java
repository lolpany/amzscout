package com.example.amzscout;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.BAD_GATEWAY;

@Aspect
public class ThrottleAspect {

    @Autowired
    private ThrottlingCache throttlingCache;

    @Around("com.example.amzscout.SystemArchitecture.throttled(request)")
    public Object throttle(ProceedingJoinPoint proceedingJoinPoint, HttpServletRequest request) throws Throwable {
        if (throttlingCache.putRequest(request)) {
            return proceedingJoinPoint.proceed();
        } else {
            throw new ResponseStatusException(BAD_GATEWAY);
        }
    }

}