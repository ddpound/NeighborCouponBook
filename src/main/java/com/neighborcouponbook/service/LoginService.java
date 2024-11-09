package com.neighborcouponbook.service;


import com.neighborcouponbook.model.vo.CouponUserVo;
import org.springframework.http.ResponseEntity;

public interface LoginService {

    /**
     * 로그인성공시 로그인 데이터를 반환
     *
     * @param couponUserVo 유저 객체안의 아이디와 비밀번호를 가져옵니다.
     *                     userLoginId
     *                     password
     *
     * */
    ResponseEntity<?> login(CouponUserVo couponUserVo);

    CouponUserVo selectCouponUserByLoginId(String userLoginId);

    Boolean passwordComparison(String dbPassword, String inputPassword);
}
