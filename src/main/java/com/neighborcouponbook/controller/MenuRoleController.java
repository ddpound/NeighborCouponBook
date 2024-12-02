package com.neighborcouponbook.controller;


import com.neighborcouponbook.common.annotation.MenuInformation;
import com.neighborcouponbook.common.response.ApiCommonResponse;
import com.neighborcouponbook.common.response.ResponseUtil;
import com.neighborcouponbook.model.search.MenuRoleSearch;
import com.neighborcouponbook.model.search.MenuSearch;
import com.neighborcouponbook.model.vo.MenuRoleVo;
import com.neighborcouponbook.service.MenuRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@RequestMapping(value = "menu-role")
@RestController
public class MenuRoleController {

    private final MenuRoleService menuRoleService;

    @Operation(
            summary = "유저의 메뉴 권한 리스트 권한 가져오기",
            description = "메뉴별 유저 권한 리스트를 가져옵니다.",
            parameters = {
                    @Parameter(
                            name = "menuSearch",
                            description = "메뉴 권한 검색 파라미터",
                            content = @Content(schema = @Schema(implementation = MenuRoleSearch.class))
                    )
            },
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
    @GetMapping(value = "list")
    public ResponseEntity<ApiCommonResponse<List<MenuRoleVo>>> selectMenuRoleList(MenuRoleSearch search){
        return ResponseUtil.createResponse(menuRoleService.selectMenuRoleVoList(search), 1, "요청에 성공했습니다", HttpStatus.OK);
    }

    @Operation(
            summary = "메뉴 권한 만들기",
            description = "메뉴 권한을 생성합니다",
            parameters = {
                    @Parameter(
                            name = "menuSearch",
                            description = "메뉴 권한 검색 파라미터",
                            content = @Content(schema = @Schema(implementation = MenuRoleSearch.class))
                    )
            },
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
    @PostMapping(value = "create")
    public ResponseEntity<ApiCommonResponse<String>> selectMenuRoleList(@RequestBody MenuRoleVo vo){
        return menuRoleService.saveMenuRole(vo);
    }

}
