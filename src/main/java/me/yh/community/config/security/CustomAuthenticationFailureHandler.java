package me.yh.community.config.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.FlashMapManager;
import org.springframework.web.servlet.support.SessionFlashMapManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * @param exception 로그인 실패 정보를 가지고 있는 객체
     */
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

        String errMsg = "가입하지 않은 아이디이거나, 잘못된 비밀번호입니다.";
        String username = request.getParameter("username");
        log.info(exception.getMessage());

        //휘발성
        final FlashMap flashMap = new FlashMap();
        flashMap.put("username", username);
        flashMap.put("errMsg", errMsg);
        final FlashMapManager flashMapManager = new SessionFlashMapManager();
        flashMapManager.saveOutputFlashMap(flashMap, request, response);

        response.sendRedirect("login");
    }
}