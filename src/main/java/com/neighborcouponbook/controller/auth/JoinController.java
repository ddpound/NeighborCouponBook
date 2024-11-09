package com.neighborcouponbook.controller.auth;


import com.neighborcouponbook.model.vo.CouponUserVo;
import com.neighborcouponbook.service.CouponUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RequiredArgsConstructor
@RequestMapping(value = "/auth/join")
@RestController
public class JoinController {

    private final CouponUserService service;

    /**
     * @param couponUserVo userloginId, username, password, remark 값을 받는다.
     *
     * */
    @PostMapping
    public ResponseEntity<?> join(@RequestBody CouponUserVo couponUserVo){
        return service.joinCouponUser(couponUserVo);
    }

}
