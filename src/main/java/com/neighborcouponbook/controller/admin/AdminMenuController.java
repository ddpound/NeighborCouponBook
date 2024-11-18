package com.neighborcouponbook.controller.admin;

import com.neighborcouponbook.common.response.ResponseUtil;
import com.neighborcouponbook.model.search.MenuSearch;
import com.neighborcouponbook.service.MenuRoleService;
import com.neighborcouponbook.service.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


    @GetMapping(value = "get/menu-list")
    public ResponseEntity<?> getMenu(MenuSearch menuSearch) {

        return ResponseUtil.createResponse(
                menuService.selectMenuList(menuSearch),
                1,
                "메뉴 반환에 성공했습니다.",
                HttpStatus.OK);
    }


}