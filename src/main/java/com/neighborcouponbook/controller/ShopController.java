package com.neighborcouponbook.controller;

import com.neighborcouponbook.model.vo.ShopVo;
import com.neighborcouponbook.service.ShopService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RequiredArgsConstructor
@RequestMapping(value = "shop")
@RestController
public class ShopController {

    private final ShopService shopService;
    
    /*
    * 가게 등록
    * userId, shopTypeId 가져와서 입력
    * */
    @PostMapping(value = "create")
    public ResponseEntity<?> createShop(@RequestBody ShopVo shopVo) {
        return shopService.createShop(shopVo);
    }

}
