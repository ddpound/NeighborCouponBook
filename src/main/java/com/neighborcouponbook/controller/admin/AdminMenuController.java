package com.neighborcouponbook.controller.admin;

import com.neighborcouponbook.common.annotation.MenuInformation;
import com.neighborcouponbook.common.response.ApiCommonResponse;
import com.neighborcouponbook.common.response.ResponseUtil;
import com.neighborcouponbook.model.search.MenuSearch;
import com.neighborcouponbook.model.vo.MenuVo;
import com.neighborcouponbook.service.MenuAuthorizationService;
import com.neighborcouponbook.service.MenuRoleService;
import com.neighborcouponbook.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 메뉴와 관련된 모든 서비스
 *
 * */
@Log4j2
@RequiredArgsConstructor
@RequestMapping(value = "admin/menu")
@RestController
public class AdminMenuController {

    private final MenuService menuService;
    private final MenuRoleService menuRoleService;
    private final MenuAuthorizationService menuAuthorizationService;

    @Operation(
            summary = "메뉴 리스트 가져오기",
            description = "등록되어있는 메뉴 정보를 가져옵니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "조회 성공",
                            useReturnTypeSchema = true
                    )
            }
    )
    @MenuInformation(
            menuAuthDetail = {
                    @MenuInformation.MenuRoleDetail(rolesName = "super-admin", roleId = 1),
            })
    @GetMapping(value = "get/menu-list")
    public ResponseEntity<ApiCommonResponse<List<MenuVo>>> getMenu(MenuSearch menuSearch) {

        return ResponseUtil.createResponse(
                menuService.selectMenuList(menuSearch),
                1,
                "메뉴 반환에 성공했습니다.",
                HttpStatus.OK);
    }

    @GetMapping(value = "cache/refresh")
    public ResponseEntity<?> refreshCache() {
        int result = menuAuthorizationService.refreshCache();

        String message = result == 1 ? "메뉴 캐싱에 성공햇습니다." : "캐싱에 실패했습니다.";

        return ResponseUtil.createResponse(null, result, message, HttpStatus.OK);
    }


}
