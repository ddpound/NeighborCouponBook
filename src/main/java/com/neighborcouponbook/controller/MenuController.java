package com.neighborcouponbook.controller;

import com.neighborcouponbook.common.annotation.MenuInformation;
import com.neighborcouponbook.common.response.ApiCommonResponse;
import com.neighborcouponbook.common.response.ResponseUtil;
import com.neighborcouponbook.model.Menu;
import com.neighborcouponbook.model.search.MenuSearch;
import com.neighborcouponbook.model.vo.CouponUserVo;
import com.neighborcouponbook.model.vo.MenuVo;
import com.neighborcouponbook.service.MenuService;
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

/**
 * @apiNote  menu 생성, 삭제, 업데이트를 담당하는 컨트롤러단
 */
@Log4j2
@RequiredArgsConstructor
@RequestMapping(value = "menu")
@RestController
public class MenuController {

    private final MenuService menuService;

    @Operation(
            summary = "메뉴 리스트 가져오기",
            description = "등록되어있는 메뉴 정보를 가져옵니다.",
            parameters = {
                    @Parameter(
                            name = "menuSearch",
                            description = "메뉴 검색 파라미터",
                            content = @Content(schema = @Schema(implementation = MenuSearch.class))
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
    public ResponseEntity<ApiCommonResponse<List<MenuVo>>> selectMenuList(MenuSearch search){
        return menuService.responseSelectMenuList(search);
    }

    @Operation(
            summary = "메뉴 생성",
            description = "메뉴를 등록합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = MenuVo.class))
            ),
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
    public ResponseEntity<ApiCommonResponse<Menu>> createMenu(@RequestBody MenuVo menuVo) {
        return menuService.createMenu(menuVo);
    }

    @Operation(
            summary = "메뉴 일괄 생성",
            description = "메뉴를 리스트 형식으로 일괄 등록합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    useParameterTypeSchema = true
            ),
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
    @PostMapping(value = "create-menu-list")
    public ResponseEntity<ApiCommonResponse<String>> createMenuList(@RequestBody List<MenuVo> menuVoList) {
        return menuService.createMenu(menuVoList);
    }
}
