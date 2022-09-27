package com.teraenergy.illegalparking.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

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

    @Autowired
    ObjectMapper objectMapper;

    @Around("execution(* com.teraenergy.illegalparking.controller..*Controller.*(..)) ")
    public Object controllerProcessing(ProceedingJoinPoint joinPoint) {
        Object object = null;
        try {
            object = joinPoint.proceed();
        } catch (Throwable e) {
            return errMsg(e.getMessage());
        }

        return object;
    }

//    @Around("execution(* com.teraenergy.illegalparking.controller..*Api.*(..)) ")
//    public Object apiProcessing(ProceedingJoinPoint joinPoint) {
//        HashMap<String, Object> result = Maps.newHashMap();
//        try {
//            result.put("success", true);
//            result.put("data", joinPoint.proceed());
//        } catch (Throwable e) {
//            result.put("success", false);
//            result.put("data", "");
//            result.put("msg", e.getMessage());
//        }
//
//        try {
//            String jsonStr = objectMapper.writeValueAsString(result);
//            return objectMapper.readValue(jsonStr, Object.class);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//
//    }

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
