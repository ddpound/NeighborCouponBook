package com.neighborcouponbook.controller.admin;

import com.neighborcouponbook.service.CouponUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RequiredArgsConstructor
@RequestMapping(value = "admin/user")
@RestController
public class AdminUserController {

    private final CouponUserService couponUserService;

    @GetMapping(value = "get/all")
    public ResponseEntity<?> getUserList(){
        return null;
    }

}
