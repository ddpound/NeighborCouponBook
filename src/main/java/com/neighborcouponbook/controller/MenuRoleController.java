package com.neighborcouponbook.controller;


import com.neighborcouponbook.common.response.ResponseUtil;
import com.neighborcouponbook.model.search.MenuRoleSearch;
import com.neighborcouponbook.model.vo.MenuRoleVo;
import com.neighborcouponbook.service.MenuRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RequiredArgsConstructor
@RequestMapping(value = "menu-role")
@RestController
public class MenuRoleController {

    private final MenuRoleService menuRoleService;


    @GetMapping(value = "list")
    public ResponseEntity<?> selectMenuRoleList(MenuRoleSearch search){
        return ResponseUtil.createResponse(menuRoleService.selectMenuRoleVoList(search), 1, "요청에 성공했습니다", HttpStatus.OK);
    }

    @PostMapping(value = "create")
    public ResponseEntity<?> selectMenuRoleList(@RequestBody MenuRoleVo vo){
        return menuRoleService.saveMenuRole(vo);
    }

}
