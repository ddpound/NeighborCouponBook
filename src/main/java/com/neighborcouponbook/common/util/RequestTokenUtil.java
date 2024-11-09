package com.neighborcouponbook.common.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;

import java.util.Optional;

@Log4j2
public class RequestTokenUtil {

    /**
     * 헤더에 들어있는 쿠키값 파싱
     * */
    public static String parseBearerToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .filter(token -> token.substring(0, 7).equalsIgnoreCase("Bearer "))
                .map(token -> token.substring(7))
                .orElse(null);
    }

    /**
     * 헤더에 값이 없다면 쿠키에서 찾아본다.
     * */
    public static String parseBearerTokenCookies(HttpServletRequest request) {
        String cookieHeader = request.getHeader(HttpHeaders.COOKIE);

        if(cookieHeader != null) {
            // 쿠키는 ; 로 구분함
            String[] cookies = cookieHeader.split("; ");
            for (String cookie : cookies) {
                // Authorization 쿠키를 찾기
                if (cookie.startsWith("Authorization=")) {
                    // "Authorization=" 뒤에 있는 값 (즉, JWT 토큰) 추출
                    String extractedToken = cookie.substring("Authorization=".length());

                    // JWT 토큰이 잘 추출되었는지 로그로 확인
                    log.info("Extracted JWT Token: {}" , extractedToken);
                    return Optional.of(extractedToken)
                            .filter(token -> token.substring(0, 7).equalsIgnoreCase("Bearer "))
                            .map(token -> token.substring(7))
                            .orElse(null);
                }
            }
        }

        return null;
    }


}
