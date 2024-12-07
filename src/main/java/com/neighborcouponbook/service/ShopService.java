package com.neighborcouponbook.service;

import com.neighborcouponbook.model.QShop;
import com.neighborcouponbook.model.vo.ShopVo;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ShopService {

    ResponseEntity<?> createShop(ShopVo shopVo);
    ResponseEntity<?> updateShop(Long shopId, ShopVo shopVo);

    ResponseEntity<?> deleteShop(Long shopId, ShopVo shopVo);

    List<ShopVo> selectShopList(Long userId);

    ShopVo selectShopInfo(Long shopId);
}
