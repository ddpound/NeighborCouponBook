package com.neighborcouponbook.service;

import com.neighborcouponbook.model.vo.ShopVo;
import org.springframework.http.ResponseEntity;

public interface ShopService {

    ResponseEntity<?> createShop(ShopVo shopVo);
}
