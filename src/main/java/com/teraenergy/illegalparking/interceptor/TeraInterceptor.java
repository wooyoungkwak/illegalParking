package com.teraenergy.illegalparking.interceptor;

import com.teraenergy.illegalparking.model.dto.user.domain.UserDto;
import com.teraenergy.illegalparking.model.dto.user.service.UserDtoService;
import com.teraenergy.illegalparking.model.entity.user.domain.User;
import com.teraenergy.illegalparking.model.entity.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Date : 2022-09-30
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@Slf4j
@RequiredArgsConstructor
@Component
public class TeraInterceptor implements HandlerInterceptor {

    private final UserService userService;

    private final UserDtoService userDtoService;
    /**
     * Controller 가 수행되기 전에
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (request != null) {
            // basic

        }

        return true;
    }

    /**
     * Controller 의 메소드가 수행된 후, View 를 호출하기 전에
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        if (modelAndView != null) {
            // now
//            LocalDateTime now = LocalDateTime.now();
//            modelAndView.getModel().put("_now", now);
//            modelAndView.getModel().put("_date", now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//            modelAndView.getModel().put("_year", now.getYear());
//            modelAndView.getModel().put("_month", now.getMonthValue());
//            modelAndView.getModel().put("_day", now.getDayOfMonth());
//            modelAndView.getModel().put("_hour", now.getHour());
//            modelAndView.getModel().put("_minute", now.getMinute());
//
//            // basic
//            modelAndView.getModel().put("_domain", request.getAttribute("_domain"));
//
//            // uri
//            modelAndView.getModel().put("_uri", request.getAttribute("_uri"));
//            modelAndView.getModel().put("_moduleCode", request.getAttribute("_moduleCode"));
//            modelAndView.getModel().put("_menuCode", request.getAttribute("_menuCode"));
//
//            // maxFileSize
//            modelAndView.getModel().put("_maxFileSize", request.getAttribute("_maxFileSize"));

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.get(auth.getName());
            UserDto userDto = userDtoService.get(user);

            // _user
            modelAndView.getModel().put("_user", userDto);
//        }
    }

    /**
     * View 작업까지 완료된 후 Client에 응답하기 직전에
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    }



}
