package com.neighborcouponbook.controller;

import com.neighborcouponbook.common.response.ResponseUtil;
import com.neighborcouponbook.model.search.MenuSearch;
import com.neighborcouponbook.model.vo.MenuVo;
import com.neighborcouponbook.service.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @apiNote  menu 생성, 삭제, 업데이트를 담당하는 컨트롤러단
 */
@Log4j2
@RequiredArgsConstructor
@RequestMapping(value = "menu")
@RestController
public class MenuController {

    private final MenuService menuService;

    @GetMapping(value = "list")
    public ResponseEntity<?> selectMenuList(MenuSearch search){
        return menuService.responseSelectMenuList(search);
    }

    @PostMapping(value = "create")
    public ResponseEntity<?> createMenu(@RequestBody MenuVo menuVo) {
        return menuService.createMenu(menuVo);
    }

    @PostMapping(value = "create-menu-list")
    public ResponseEntity<?> createMenuList(@RequestBody List<MenuVo> menuVoList) {
        return menuService.createMenu(menuVoList);
    }
}
