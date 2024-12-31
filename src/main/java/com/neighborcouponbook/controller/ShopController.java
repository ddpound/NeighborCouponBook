package com.neighborcouponbook.controller;

import com.neighborcouponbook.common.annotation.MenuInformation;
import com.neighborcouponbook.common.response.ResponseUtil;
import com.neighborcouponbook.model.search.ShopTypeSearch;
import com.neighborcouponbook.model.vo.ShopVo;
import com.neighborcouponbook.service.ShopService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping(value = "shop")
@RestController
public class ShopController {

    private final ShopService shopService;

    @Operation(
            summary = "가게등록",
            description = "userId, shopTypeId 가져와서 입력",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = ShopVo.class))
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
                    @MenuInformation.MenuRoleDetail(rolesName = "user", roleId = 2)
            })
    @PostMapping(value = "create")
    public ResponseEntity<?> createShop(@RequestBody ShopVo shopVo) {
        return shopService.createShop(shopVo);
    }

    @Operation(
            summary = "가게업데이트",
            description = "가게 데이터를 업데이트를 합니다",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = ShopVo.class))
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
                    @MenuInformation.MenuRoleDetail(rolesName = "user", roleId = 2)
            })
    @PostMapping(value = "update")
    public ResponseEntity<?> updateShop(@RequestParam(value = "id") Long shopId, @RequestBody ShopVo shopVo) { return shopService.updateShop(shopId, shopVo); }

    @PostMapping(value = "delete")
    public ResponseEntity<?> deleteShop(@RequestParam(value = "id") Long shopId, @RequestBody ShopVo shopVo) { return shopService.deleteShop(shopId, shopVo); }

    // 사장시점 - 샵 리스트
    @GetMapping(value = "shops")
    public List<ShopVo> selectShopListOfUser(@RequestParam(value = "id") Long userId) { return shopService.selectShopList(userId); };

    //  shop detail page
    @GetMapping(value = "info")
    public ShopVo selectShopInfo(@RequestParam Long shopId) { return shopService.selectShopInfo(shopId); }

    @Operation(
            summary = "가게 타입 생성",
            description = "가게 타입을 생성합니다",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = ShopVo.class))
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
                    @MenuInformation.MenuRoleDetail(rolesName = "user", roleId = 2)
            })
    @PostMapping(value = "type/create")
    public ResponseEntity<?> createShopType(){
        return ResponseUtil.createResponse(null,null,1,"반환성공", HttpStatus.OK);
    }

    @Operation(
            summary = "가게 타입 리스트를 가져옵니다",
            description = "가게 타입 데이터 리스트를 가져옵니다",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = ShopVo.class))
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
                    @MenuInformation.MenuRoleDetail(rolesName = "user", roleId = 2)
            })
    @GetMapping(value = "type/list")
    public ResponseEntity<?> selectShopTypeList(ShopTypeSearch search) {
        return shopService.selectShopTypeList(search);
    }
}
