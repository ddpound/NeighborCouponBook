package com.neighborcouponbook.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RequiredArgsConstructor
@RequestMapping(value = "test")
@RestController
public class TestController {

    @GetMapping(value = "login-test")
    public String test() {
        return "login-test 완료";
    }

}
