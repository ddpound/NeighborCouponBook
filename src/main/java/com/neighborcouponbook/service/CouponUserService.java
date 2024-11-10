package com.neighborcouponbook.service;

import com.neighborcouponbook.model.CouponUser;
import com.neighborcouponbook.model.search.CouponUserSearch;
import com.neighborcouponbook.model.vo.CouponUserVo;
import com.neighborcouponbook.model.vo.CouponUserWithUserRole;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CouponUserService {

    Integer createCouponUser(CouponUser couponUser);

    /**
     * 컨트롤러단에서 직접받아 처리하는 메소드
     * 유저 생성
     * */
    ResponseEntity<?> createCouponUser(CouponUserVo couponUserVo);

    /**
     * 회원가입(초기 요청)
     * */
    ResponseEntity<?> joinCouponUser(CouponUserVo couponUserVo);

    /**
     * 로그인 아이디 중복체크
     * @return boolean 참이면 중복이 있음, 거짓이면 없음
     * */
    Boolean loginIdDuplicateCheck(CouponUserVo couponUserVo);

    /**
     * 로그인시 객체 검증 함수
     * */
    ResponseEntity<?> couponUserVoValidation(CouponUserVo couponUserVo);

    List<CouponUserVo> selectCouponUserList(CouponUserSearch search);

    CouponUserVo selectCouponUser(CouponUserSearch search);

    /**
     * NOTE : 생성할 유저 객체 전체 세팅 (유제 데이터 , 주석)
     * @param couponUserVo
     * */
    CouponUser settingCreateUserWithCreateData(CouponUserVo couponUserVo, Long insertUserId);

    /**
     * @param search 사용자 검색 정보를 담고있음.
     *               userLoginId : 로그인 아이디로 검색을 진행
     *
     * */
    String selectUserPassword(CouponUserSearch search);

    BooleanBuilder settingCouponUserSearchBuilder(CouponUserSearch search);

    JPAQuery<CouponUser> selectCouponUserQuery(CouponUserSearch search);


    /**
     * UserRole table과 join select 문
     *
     * */
    List<CouponUserWithUserRole> selectCouponUserQueryJoinUserRole(CouponUserSearch search);
}
