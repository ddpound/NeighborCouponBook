package com.neighborcouponbook.filters;

import com.neighborcouponbook.common.service.JwtService;
import com.neighborcouponbook.common.util.RequestTokenUtil;
import com.neighborcouponbook.model.search.MenuRoleSearch;
import com.neighborcouponbook.model.search.MenuSearch;
import com.neighborcouponbook.model.vo.MenuRoleVo;
import com.neighborcouponbook.model.vo.MenuVo;
import com.neighborcouponbook.service.CouponUserService;
import com.neighborcouponbook.service.MenuRoleService;
import com.neighborcouponbook.service.MenuService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * 메뉴 권한을 확인하는 필터
 * superAdmin은 권한을 체크하지 않고 모두 통과시켜준다.
 *
 * */

@Log4j2
@RequiredArgsConstructor
@Component
public class MenuAuthenticationFilter implements Filter {

    @Value("${admin-setting.super-admin-id}")
    private Long superAdminId;

    @Value("${admin-setting.super-admin-login-id}")
    private String superAdminLoginId;

    @Value("${admin-setting.super-admin-pw}")
    private String superAdminPassword;

    @Value("${admin-setting.super-admin-role-name}")
    private String superAdminRoleName;

    @Value("${admin-setting.super-admin-role-id}")
    private Long superAdminRoleId;

    @Value("${filter-setting.menu-auth-filter}")
    private String menuAuthFilter;

    private final CouponUserService couponUserService;

    private final MenuService menuService;

    private final MenuRoleService menuRoleService;

    private final JwtService jwtService;

    /**
     * 권한 체크 필터, 모든 권한을 체크해준다.
     * 1. super-admin 이면 권한 체크를 하지 않고 넘어간다.
     *
     * */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        try {
            // 요청 IP 주소 가져오기
            String clientIp = request.getRemoteAddr();
            String xForwardedForHeader = request.getHeader("X-Forwarded-For");
            if (xForwardedForHeader != null) {
                clientIp = xForwardedForHeader.split(",")[0].trim();
            }
            log.info("Client IP: {}", clientIp);

            /**
             * URI 추출 및 권한 확인 로직
             * method 확인
             * */
            String requestUri = ((HttpServletRequest) servletRequest).getRequestURI();
            log.info("Request URI : {} ", requestUri);
            StringBuilder uri = new StringBuilder(requestUri);

            if(uri.toString().startsWith("http")) {
                uri = new StringBuilder();

                // 1. http cutting
                String[] fristCut = requestUri.split("//");

                // 2. / slash cutting
                String[] secondCut = fristCut[1].split("/");

                // 3. domain cutting
                for (int i = 1; i < secondCut.length; i++) {
                    uri.append(secondCut[i]);
                }
            }

            String token = RequestTokenUtil.parseBearerToken(request);

            // 값이 없다면 쿠키값도 한번더 체크
            if(token == null) {
                log.info("token is null, check cookies");
                token = RequestTokenUtil.parseBearerTokenCookies(request);
            }

            // 토큰 검증, 에러 발생시 403
            Claims tokenClaims = jwtService.validateTokenAndGetSubject(token);

            if(menuAuthFilter.equals("true")){
                // auth로 시작하면 통과
                if(uri.toString().startsWith("/auth") || Long.parseLong(tokenClaims.get("role_id").toString()) == superAdminRoleId){
                    filterChain.doFilter(servletRequest, servletResponse);
                }else{
                    MenuSearch menuSearch = MenuSearch.builder().menuUri(requestUri.trim()).pageNumber(1L).pageSize(10L).build();
                    List<MenuVo> menuVoList = menuService.selectMenuList(menuSearch);
                    log.info("menu role list : {} ", menuVoList);
                    if(menuVoList != null && !menuVoList.isEmpty()){
                        // menu uri 는 중복이 불가능함.
                        MenuRoleSearch menuRoleSearch = MenuRoleSearch
                                .builder()
                                .menuId(menuVoList.get(0).getMenuId())
                                .roleId(Long.parseLong(tokenClaims.get("role_id").toString()))
                                .build();
                        List<MenuRoleVo> menuRoleVoList = menuRoleService.selectMenuRoleVoList(menuRoleSearch);
                        log.info("menu role list : {} ", menuRoleVoList);

                        // 권한 체크 시작
                        // HTTP 메서드 확인 (GET, POST, PUT, DELETE 등)
                        String method = request.getMethod().toLowerCase();
                        if(menuRoleVoList != null && !menuRoleVoList.isEmpty()){
                            boolean result = false;
                            switch (method){
                                case "get":
                                    if (menuRoleVoList.get(0).getRead()){
                                        result = true;
                                    }
                                    break;
                                case "post", "delete", "put":
                                    if (menuRoleVoList.get(0).getWrite()){
                                        result = true;
                                    }
                                    break;
                            }

                            log.info("result : {}, {} ", result, method);
                            if(result){
                                filterChain.doFilter(servletRequest, servletResponse);
                            }else{
                                response.sendError(HttpServletResponse.SC_FORBIDDEN, "메뉴에 관련된 권한이 없습니다.");
                            }

                        }else{
                            response.sendError(HttpServletResponse.SC_FORBIDDEN, "메뉴에 관련된 권한이 없습니다.");
                        }


                    }else{
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "등록된 메뉴가 없습니다.");
                    }
                }
            }else{
                filterChain.doFilter(servletRequest, servletResponse);
            }
        }catch (Exception e) {
            log.error(e.getMessage());
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "에러가 발생했습니다.");
        }
    }
}
