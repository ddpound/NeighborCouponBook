package com.neighborcouponbook.service.impl;

import com.neighborcouponbook.common.components.AuthorizationResult;
import com.neighborcouponbook.common.context.RequestContext;
import com.neighborcouponbook.common.service.JwtService;
import com.neighborcouponbook.service.MenuRoleService;
import com.neighborcouponbook.service.MenuService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.*;

@Log4j2
@ExtendWith(MockitoExtension.class)
public class MenuAuthorizationServiceImplTest {

    @Mock
    private MenuService menuService;

    @Mock
    private MenuRoleService menuRoleService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private MenuAuthorizationServiceImpl menuAuthorizationService;

    @BeforeEach
    void setUp() {
        // 필요한 필드 설정값 주입
        ReflectionTestUtils.setField(menuAuthorizationService, "superAdminRoleId", 1L);
        ReflectionTestUtils.setField(menuAuthorizationService, "cacheEnabled", false);
        ReflectionTestUtils.setField(menuAuthorizationService, "cacheSize", 100);
    }

    /**
     * 시나리오
     * 1. auth 로 시작되는 엔드포인트 접근시도
     * */
    @Test
    void authorizePublicEndpointShouldReturnAuthorized() {
        RequestContext context = new RequestContext("127.0.0.1",
                "/auth/login", "dummy-token","GET",
                null,null,null);

        AuthorizationResult result = menuAuthorizationService.authorize(context);

        // Then
        assertThat(result.isAuthorized()).isTrue();
    }

    @Test
    void authorizePrivateEndpointShouldReturnAuthorized() {
        RequestContext context = new RequestContext("127.0.0.1",
                "/coupon-user/get/my-data", "dummy-token","GET",
                null,null,null);

        AuthorizationResult result = menuAuthorizationService.authorize(context);

        // Then
        assertThat(result.isAuthorized()).isFalse();
    }

}
