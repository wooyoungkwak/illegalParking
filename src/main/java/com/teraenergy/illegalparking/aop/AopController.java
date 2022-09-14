package com.teraenergy.illegalparking.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Date : 2022-09-14
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
@Aspect
@Component
public class AopController {
    @Around("execution(* com.teraenergy.illegalparking.controller..*(..)) ")
    public Object homeProcessing(ProceedingJoinPoint joinPoint) {
        Object object = null;
        try {
            object = joinPoint.proceed();
        } catch (Throwable e) {
            return errMsg(e.getMessage());
        }

        return object;
    }

    @Around("execution(* com.teraenergy.illegalparking.controller.login.LoginController.*(..)) ")
    public Object loginProcessing(ProceedingJoinPoint joinPoint) {
        Object object = null;
        try {
            object = joinPoint.proceed();
        } catch (Throwable e) {
            return errMsg(e.getMessage());
        }

        return object;
    }


    /**
     *  @AfterThrows 방법를 이용하는 방법도 있음.
     */
    public Object errMsg(String message){
        // HttpServletRequest 접근 방법
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        request.setAttribute("msg", message);
        return "/controller/error/500";
    }
}
