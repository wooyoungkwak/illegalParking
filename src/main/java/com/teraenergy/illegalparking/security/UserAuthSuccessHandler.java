package com.teraenergy.illegalparking.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Date : 2022-09-20
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
@Slf4j
@Component
public class UserAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        log.info(" ====================> onAuthenticationSuccess .... ");

        request.getSession().setAttribute("hashcode", request.getSession().getId().hashCode());

        // default targetUrl 로 이동하지 않도록 설정
        super.setAlwaysUseDefaultTargetUrl(true);

        // default 페이지 설정
        super.setDefaultTargetUrl("/");

        // 페이지 이동
        super.onAuthenticationSuccess(request, response, authentication);

//        // targetUrl 파라메터 이름 설정
//        super.setTargetUrlParameter("redirectUrl");

    }

}

