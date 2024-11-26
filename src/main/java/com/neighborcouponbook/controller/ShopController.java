package com.neighborcouponbook.controller;

import com.neighborcouponbook.model.Shop;
import com.neighborcouponbook.model.vo.ShopVo;
import com.neighborcouponbook.service.ShopService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping(value = "update")
    public ResponseEntity<?> updateShop(@RequestParam(value = "id") Long shopId, @RequestBody ShopVo shopVo) { return shopService.updateShop(shopId, shopVo); }

    @PostMapping(value = "delete")
    public ResponseEntity<?> deleteShop(@RequestParam(value = "id") Long shopId, @RequestBody ShopVo shopVo) { return shopService.deleteShop(shopId, shopVo); }

    @GetMapping(value = "shops")
    public List<ShopVo> selectShopListOfUser(@RequestParam(value = "id") Long userId) { return shopService.selectShopList(userId); };
}
