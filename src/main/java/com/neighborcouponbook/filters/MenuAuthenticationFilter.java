package com.neighborcouponbook.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neighborcouponbook.common.components.AuthorizationResult;
import com.neighborcouponbook.common.context.RequestContext;
import com.neighborcouponbook.common.response.ApiResponse;
import com.neighborcouponbook.common.response.ResponseUtil;
import com.neighborcouponbook.common.service.JwtService;
import com.neighborcouponbook.common.util.RequestTokenUtil;
import com.neighborcouponbook.model.search.MenuRoleSearch;
import com.neighborcouponbook.model.search.MenuSearch;
import com.neighborcouponbook.model.vo.MenuRoleVo;
import com.neighborcouponbook.model.vo.MenuVo;
import com.neighborcouponbook.service.CouponUserService;
import com.neighborcouponbook.service.MenuAuthorizationService;
import com.neighborcouponbook.service.MenuRoleService;
import com.neighborcouponbook.service.MenuService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 메뉴 권한을 확인하는 필터
 * superAdmin은 권한을 체크하지 않고 모두 통과시켜준다.
 *
 * */

@Log4j2
@RequiredArgsConstructor
@Component
public class MenuAuthenticationFilter extends OncePerRequestFilter {

    @Value("${menu.filter-setting.menu-auth-filter}")
    private boolean menuAuthFilter;

    private final MenuAuthorizationService menuAuthorizationService;

    /**
     * 권한 체크 필터, 모든 권한을 체크해준다.
     * 1. super-admin 이면 권한 체크를 하지 않고 넘어간다.
     *
     * */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // 요청 IP 주소 가져오기
            String clientIp = request.getRemoteAddr();
            String xForwardedForHeader = request.getHeader("X-Forwarded-For");
            if (xForwardedForHeader != null) {
                clientIp = xForwardedForHeader.split(",")[0].trim();
            }
            log.info("Client IP: {}", clientIp);

            String requestUri = request.getRequestURI();
            log.info("Request URI : {} ", requestUri);
            StringBuilder uri = new StringBuilder(requestUri);

            if(uri.toString().startsWith("http")) {
                uri = new StringBuilder();
                String[] fristCut = requestUri.split("//");
                String[] secondCut = fristCut[1].split("/");
                for (int i = 1; i < secondCut.length; i++) {
                    uri.append(secondCut[i]);
                }
            }

            String token = RequestTokenUtil.parseBearerToken(request);

            if(token == null) {
                log.info("token is null, check cookies");
                token = RequestTokenUtil.parseBearerTokenCookies(request);
            }

            String method = request.getMethod().toLowerCase();

            RequestContext requestContext = RequestContext.builder()
                    .clientIp(clientIp)
                    .requestUri(requestUri)
                    .token(token)
                    .method(method)
                    .build();

            if(menuAuthFilter) {
                AuthorizationResult result = menuAuthorizationService.authorize(requestContext);

                if (result.isAuthorized()) {
                    filterChain.doFilter(request, response);
                } else {
                    log.info("권한 획득 실패 : {}", result.getErrorMessage());
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write(ResponseUtil.responseEntityMapperString(null,null,-1,result.getErrorMessage(), HttpStatus.FORBIDDEN));
                }
            } else {
                filterChain.doFilter(request, response);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(ResponseUtil.responseEntityMapperString(null,null,-1, "에러가 발생했습니다.", HttpStatus.FORBIDDEN));
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String[] excludePath = {
                "/auth/main",
                "/auth/user/login",
                "/auth/user/join",
                "/auth/test/welcome",
                "/auth/test/user/lubid",
                "/public/file/**",
                "/swagger-ui",
                "/api-docs",
                "/swagger-ui.html"
        };

        String path = request.getRequestURI();
        PathMatcher pathMatcher = new AntPathMatcher();

        return Arrays.stream(excludePath).anyMatch(path::startsWith);
    }
}
