package com.neighborcouponbook.controller;


import com.neighborcouponbook.common.annotation.MenuInformation;
import com.neighborcouponbook.common.response.ApiCommonResponse;
import com.neighborcouponbook.common.response.ResponseUtil;
import com.neighborcouponbook.common.util.AuthUtil;
import com.neighborcouponbook.model.search.CouponUserSearch;
import com.neighborcouponbook.model.vo.CouponUserVo;
import com.neighborcouponbook.model.vo.CouponUserWithUserRole;
import com.neighborcouponbook.service.CouponUserService;
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
import java.util.Objects;

@Log4j2
@RequiredArgsConstructor
@RequestMapping(value = "coupon-user")
@RestController
public class CouponUserController {

    private final CouponUserService couponUserService;

    @GetMapping(value = "login-test")
    public ResponseEntity<?> loginTest() {
        return ResponseUtil
                .createResponse(
                        AuthUtil.getLoginUserData(),
                        1,
                        "로그인에 성공하여 올바르게 요청이 반환되었습니다.",
                        HttpStatus.OK);
    }

    @Operation(
            summary = "내 유저 정보 가져오기",
            description = "내 유저 정보만 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "조회 성공",
                            useReturnTypeSchema = true
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "서버 에러, -1 으로 반환합니다.",
                            useReturnTypeSchema = true
                    )
            }
    )
    @MenuInformation(
            menuAuthDetail = {
                    @MenuInformation.MenuRoleDetail(rolesName = "super-admin", roleId = 1),
                    @MenuInformation.MenuRoleDetail(rolesName = "user", roleId = 2)
            })
    @GetMapping(value = "get/data")
    public ResponseEntity<ApiCommonResponse<List<CouponUserVo>>> getUserData(CouponUserSearch search) {

        List<CouponUserVo> selectUserVoList = couponUserService.selectCouponUserList(search);

        if(selectUserVoList != null && !selectUserVoList.isEmpty()) {
            return ResponseUtil.createResponse(
                    selectUserVoList,
                    1,
                    "리스트 반환 완료",
                    HttpStatus.OK
            );
        }

        return ResponseUtil.createResponse(
                null,
                -1,
                "데이터가 없습니다.",
                HttpStatus.OK);
    }


    @Operation(
            summary = "내 유저 정보 (권한 포함) 가져오기",
            description = "내 유저 정보와 권한 정보를 모두 가져옵니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "조회 성공",
                            useReturnTypeSchema = true
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 에러",
                            useReturnTypeSchema = true
                    )
            }
    )
    @MenuInformation(
            menuAuthDetail = {
                    @MenuInformation.MenuRoleDetail(rolesName = "super-admin", roleId = 1),
                    @MenuInformation.MenuRoleDetail(rolesName = "user", roleId = 2)
    })
    @GetMapping(value = "get/my-data")
    public ResponseEntity<ApiCommonResponse<List<CouponUserWithUserRole>>> getUserAllData(CouponUserSearch search) {

        Long nowLoginId = Objects.requireNonNull(AuthUtil.getLoginUserData()).getUserId();

        if(nowLoginId != null) {
            search.setUserId(nowLoginId);

            List<CouponUserWithUserRole> selectUserWithUserRoleList =
                    couponUserService.selectCouponUserQueryJoinUserRole(search);

            if(selectUserWithUserRoleList != null && !selectUserWithUserRoleList.isEmpty()) {
                return ResponseUtil.createResponse(
                        selectUserWithUserRoleList,
                        1,
                        "리스트 반환 완료",
                        HttpStatus.OK
                );
            }
        }

        return ResponseUtil.createResponse(null,-1, "데이터가 없습니다", HttpStatus.OK);
    }

    @Operation(
            summary = "유저 생성",
            description = "유저를 생성합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = CouponUserVo.class))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "조회 성공",
                            useReturnTypeSchema = true
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 에러"
                    )
            }
    )
    @MenuInformation(
            menuAuthDetail = {
                    @MenuInformation.MenuRoleDetail(rolesName = "super-admin", roleId = 1),
            })
    @PostMapping(value = "create")
    public ResponseEntity<ApiCommonResponse<String>> create(@RequestBody CouponUserVo couponUserVo) {
        return couponUserService.createCouponUser(couponUserVo);
    }
}
