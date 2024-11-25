package com.neighborcouponbook.filters;



import com.neighborcouponbook.common.service.JwtService;
import com.neighborcouponbook.common.util.RequestTokenUtil;
import com.neighborcouponbook.config.auth.PrincipalDetail;
import com.neighborcouponbook.model.CouponUser;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;


@Log4j2
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = RequestTokenUtil.parseBearerToken(request);

        // 값이 없다면 쿠키값도 한번더 체크
        if(token == null) {
            log.info("token is null, check cookies");
            token = RequestTokenUtil.parseBearerTokenCookies(request);
        }

        if(token != null) {
            Claims tokenClaims = jwtService.validateTokenAndGetSubject(token); // 토큰 검증, 에러 발생시 403

            if(tokenClaims != null) {
                // 토큰 검증과 동시에 아이디 값과 ROLE값을 넣어준다.
                PrincipalDetail principalDetail = new PrincipalDetail(
                        CouponUser.builder()
                                .userName(tokenClaims.get("user_name").toString())
                                .userId(Long.parseLong(tokenClaims.get("user_id").toString()))
                                .build());

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(principalDetail, null, principalDetail.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                logger.info("request user id : " + tokenClaims.get("user_id").toString());
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 제외 url
     * */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String[] excludePath = {
                "/auth/main",
                "/auth/user/login",
                "/auth/user/join",
                "/auth/test/welcome",
                "/auth/test/user/lubid",
                "/public/file/**"
        };

        String path = request.getRequestURI();
        PathMatcher pathMatcher = new AntPathMatcher();

        return Arrays.stream(excludePath).anyMatch(path::startsWith);
    }
}
