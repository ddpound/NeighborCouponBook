package com.neighborcouponbook.controller;


import com.neighborcouponbook.common.response.ResponseUtil;
import com.neighborcouponbook.common.util.AuthUtil;
import com.neighborcouponbook.model.search.CouponUserSearch;
import com.neighborcouponbook.model.vo.CouponUserVo;
import com.neighborcouponbook.model.vo.CouponUserWithUserRole;
import com.neighborcouponbook.service.CouponUserService;
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
                        "로그인에 성공해 올바르게 요청이 반환되었습니다.",
                        HttpStatus.OK);
    }

    /**
     * @return userData 만 가져옴
     * */
    @GetMapping(value = "get/data")
    public ResponseEntity<?> getUserData(CouponUserSearch search) {

        List<CouponUserVo> selectUserVoList = couponUserService.selectCouponUserList(search);

        if(selectUserVoList != null && !selectUserVoList.isEmpty()) {
            return ResponseUtil.createResponse(
                    selectUserVoList,
                    1,
                    "리스트 반환 완료",
                    HttpStatus.OK
            );
        }


        return ResponseUtil.createSuccessResponse(-1, "데이터가 없습니다");
    }


    /**
     * 로그인 되어있는 내 계정의 데이터만을 가져옴
     * @return userData와 권한 및 관련된 데이터를 모두 가져옴
     * */
    @GetMapping(value = "get/my-data")
    public ResponseEntity<?> getUserAllData(CouponUserSearch search) {

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

        return ResponseUtil.createSuccessResponse(-1, "데이터가 없습니다");
    }

    @PostMapping(value = "create")
    public ResponseEntity<?> create(@RequestBody CouponUserVo couponUserVo) {
        return couponUserService.createCouponUser(couponUserVo);
    }
}
