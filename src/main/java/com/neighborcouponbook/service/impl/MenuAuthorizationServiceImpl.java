package com.neighborcouponbook.service.impl;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.neighborcouponbook.common.components.AuthorizationResult;
import com.neighborcouponbook.common.context.RequestContext;
import com.neighborcouponbook.common.service.JwtService;
import com.neighborcouponbook.model.Menu;
import com.neighborcouponbook.model.search.MenuRoleSearch;
import com.neighborcouponbook.model.search.MenuSearch;
import com.neighborcouponbook.model.vo.MenuRoleVo;
import com.neighborcouponbook.model.vo.MenuVo;
import com.neighborcouponbook.service.MenuAuthorizationService;
import com.neighborcouponbook.service.MenuRoleService;
import com.neighborcouponbook.service.MenuService;
import io.jsonwebtoken.Claims;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.Cacheable;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class MenuAuthorizationServiceImpl implements MenuAuthorizationService {
    private final MenuService menuService;
    private final MenuRoleService menuRoleService;
    private final JwtService jwtService;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Value("${admin-setting.super-admin-role-id}")
    private Long superAdminRoleId;

    @Value("${menu.cache.enabled}")
    private boolean cacheEnabled;

    @Value("${menu.cache.size}")
    private int cacheSize;

    private LoadingCache<String, Optional<List<MenuVo>>> menuCache;

    @PostConstruct
    public void init() {
        if (cacheEnabled) {
            menuCache = Caffeine.newBuilder()
                    .maximumSize(cacheSize)
                    .expireAfterWrite(1, TimeUnit.HOURS)
                    .build(this::loadMenu);
        }
    }

    // 캐시 전체를 새로고침하는 메서드
    @Override
    public Integer refreshCache() {
        try {
            if (cacheEnabled && menuCache != null) {
                menuCache.invalidateAll();
            }
            return 1;
        }catch (Exception e) {
            log.error(e.getMessage());
            return -1;
        }
    }

    private Optional<List<MenuVo>> loadMenu(String requestUri) {
        return Optional.ofNullable(menuService.selectAllMenus());
    }

    public AuthorizationResult authorize(RequestContext context) {
        if (isPublicEndpoint(context.getRequestUri())) {
            return AuthorizationResult.authorized();
        }

        Claims claims = jwtService.validateTokenAndGetSubject(context.getToken());
        Long roleId = Long.parseLong(claims.get("role_id").toString());

        if (roleId.equals(superAdminRoleId)) {
            return AuthorizationResult.authorized();
        }

        return checkMenuAuthorization(context, roleId);
    }

    private boolean isPublicEndpoint(String uri) {
        return uri.startsWith("/auth");
    }

    /**
     * 메뉴 접근 권한을 확인하는 메서드
     * @param context 요청 컨텍스트 정보
     * @param roleId 사용자 역할 ID
     * @return 권한 검사 결과
     */
    private AuthorizationResult checkMenuAuthorization(RequestContext context, Long roleId) {
        // 캐시 사용 여부에 따라 메뉴 정보 조회 방식 결정
        Optional<List<MenuVo>> menuOpt = cacheEnabled
                ? menuCache.get(context.getRequestUri())  // 캐시에서 조회
                : loadMenu(context.getRequestUri());      // DB에서 직접 조회

        // 쿼리스트링 제거 된 문자열
        String removeRequestUri = removeQueryParameters(context.getRequestUri());

        // list static, dynamic 구분 리스트
        if(menuOpt.isPresent()) {
            List<MenuVo> menus = searchFilteringMenuList(menuOpt.get(),removeRequestUri);


            if(menus == null) return AuthorizationResult.denied("No matching menu found");
            if(menus.size() > 1) return AuthorizationResult.denied("Multiple matching menus found");

            MenuRoleVo menuRole = menuRoleService.selectMenuRole(
                    MenuRoleSearch.builder()
                            .menuId(menus.get(0).getMenuId())
                            .roleId(roleId)
                            .build());

            if (menuRole == null) {
                return AuthorizationResult.denied("No menu role found");  // 권한 없음
            }

            // HTTP 메서드에 따른 권한 확인
            return checkMethodPermission(context.getMethod(), menuRole);
        }else{
            return AuthorizationResult.denied("not fount menu list");  // 메뉴를 찾을 수 없음.
        }
    }

    public List<MenuVo> searchFilteringMenuList(List<MenuVo> menuVoList, String requestUri){
        Menu.MenuProperty menuProperty = checkMenuType(requestUri, menuVoList);

        List<MenuVo> menus = null;

        switch (menuProperty){
            case STATIC:
                menus = menuVoList
                        .stream()
                        .filter(menu -> menu.getMenuProperty() == Menu.MenuProperty.STATIC)
                        .filter(menu -> menu.getMenuUri().equals(requestUri))
                        .collect(Collectors.toList());
                break;
            case DYNAMIC:
                menus = menuVoList
                        .stream()
                        .filter(menu -> menu.getMenuProperty() == Menu.MenuProperty.DYNAMIC)
                        .filter(menu -> matchesDynamicPattern(requestUri, menu.getMenuUri()))
                        .collect(Collectors.toList());
                break;
        }

        return menus;
    }

    public Menu.MenuProperty checkMenuType(String requestUri, List<MenuVo> menuUris) {
        // 1. 정적 URI 먼저 확인
        if (menuUris.stream()
                .filter(menu -> menu.getMenuProperty() == Menu.MenuProperty.STATIC)
                .anyMatch(menu -> menu.getMenuUri().equals(requestUri))) {
            return Menu.MenuProperty.STATIC;
        }

        // 2. 동적 URI 패턴 확인
        if(menuUris.stream()
                .filter(menu -> menu.getMenuProperty() == Menu.MenuProperty.DYNAMIC)
                .anyMatch(menu -> matchesDynamicPattern(requestUri, menu.getMenuUri()))){
            return Menu.MenuProperty.DYNAMIC;
        }

        return null;
    }

    private String removeQueryParameters(String uri) {
        int queryIndex = uri.indexOf('?');
        return queryIndex != -1 ? uri.substring(0, queryIndex) : uri;
    }

    private boolean matchesDynamicPattern(String requestUri, String pattern) {
        // URI 템플릿을 정규식 패턴으로 변환
        String regex = pattern.replaceAll("\\{[^}]+\\}", "[^/]+");
        return requestUri.matches(regex);
    }

    private MenuVo findMatchingMenu(List<MenuVo> menus, String requestUri) {
        return menus.stream()
                .filter(menu -> pathMatcher.match(menu.getMenuUri(), requestUri))
                .findFirst()
                .orElse(null);
    }

    private AuthorizationResult checkMethodPermission(String method, MenuRoleVo menuRole) {
        boolean hasPermission = switch (method.toLowerCase()) {
            case "get" -> menuRole.getRead();
            case "post", "put", "delete" -> menuRole.getWrite();
            default -> false;
        };

        return hasPermission
                ? AuthorizationResult.authorized()
                : AuthorizationResult.denied("Insufficient permissions for this operation");
    }
}

