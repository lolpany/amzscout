package com.example.amzscout;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import javax.servlet.http.HttpServletRequest;

@Aspect
public class SystemArchitecture {

    @Pointcut("@annotation(com.example.amzscout.Throttled) && args(request,..)")
    public void throttled(HttpServletRequest request) {
    }
}