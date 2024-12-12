package com.neighborcouponbook.service.impl;

import com.neighborcouponbook.common.components.CommonBCryptPasswordEncoder;
import com.neighborcouponbook.common.response.ResponseUtil;
import com.neighborcouponbook.common.service.JwtService;
import com.neighborcouponbook.common.util.NullChecker;
import com.neighborcouponbook.model.CouponUser;
import com.neighborcouponbook.model.search.CouponUserSearch;
import com.neighborcouponbook.model.search.UserRoleSearch;
import com.neighborcouponbook.model.vo.CouponUserVo;
import com.neighborcouponbook.model.vo.UserRoleVo;
import com.neighborcouponbook.service.CouponUserService;
import com.neighborcouponbook.service.LoginService;
import com.neighborcouponbook.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Service
public class LoginServiceImpl implements LoginService {

    private final CouponUserService userService;

    private final UserRoleService userRoleService;

    private final CommonBCryptPasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<?> login(CouponUserVo userVo) {

        log.info("try userLoginId : {}", userVo.getUserLoginId());

        if(NullChecker.isNull(userVo) || NullChecker.isNull(userVo.getUserLoginId()) || NullChecker.isNull(userVo.getPassword()))
            return ResponseUtil.createResponse(null, -1,"비밀번호나 패스워드를 입력해주세요.", HttpStatus.BAD_REQUEST);

        String userLoginId = userVo.getUserLoginId();
        String password = userVo.getPassword();

        CouponUserVo selectVo = selectCouponUserByLoginId(userLoginId);

        // 1. 로그인 아이디가 없음
        if(selectVo == null)
            return ResponseUtil.createResponse(null, -1,"해당 아이디가 없습니다", HttpStatus.BAD_REQUEST);

        CouponUserSearch search = new CouponUserSearch();
        search.setUserLoginId(userLoginId);
        String selectPassword = userService.selectUserPassword(search);

        // 2.password 검사
        if(!passwordEncoder.getBCryptPasswordEncoder().matches(password, selectPassword))
            return ResponseUtil.createResponse(null, -1,"패스워드가 틀립니다.", HttpStatus.BAD_REQUEST);

        // 검증 완료 토큰 생성 시작
        CouponUser couponUser = new CouponUser();
        couponUser.initCouponUser(selectVo.getUserId(), selectVo.getUserLoginId(),selectVo.getUserName(),selectPassword);

        // user의 role 검색
        List<UserRoleVo> userRoleVo = userRoleService.selectUserRoleList(UserRoleSearch.builder().userId(selectVo.getUserId()).build());

        selectVo.setToken(jwtService.createToken(couponUser, userRoleVo));

        return ResponseUtil
                .createResponse(
                        selectVo,
                        1,
                        "로그인에 성공했습니다.",
                        HttpStatus.OK);
    }

    @Override
    public CouponUserVo selectCouponUserByLoginId(String userLoginId) {
        CouponUserSearch search = new CouponUserSearch();
        search.setUserLoginId(userLoginId);

        return userService.selectCouponUser(search);
    }

    @Override
    public Boolean passwordComparison(String inputPassword, String dbPassword) {
        return passwordEncoder.getBCryptPasswordEncoder().matches(inputPassword, dbPassword);
    }

}
