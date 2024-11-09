package com.neighborcouponbook.controller.auth;

import com.neighborcouponbook.common.service.JwtService;
import com.neighborcouponbook.model.vo.CouponUserVo;
import com.neighborcouponbook.service.CouponUserService;
import com.neighborcouponbook.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RequiredArgsConstructor
@RequestMapping(value = "auth/login")
@RestController
public class LoginController {

    private final CouponUserService couponUserService;

    private final LoginService loginService;

    private final JwtService jwtService;

    /**
     * @param userVo
     * */
    @PostMapping
    public ResponseEntity<?> login(@RequestBody CouponUserVo userVo){
        return loginService.login(userVo);
    }

}
