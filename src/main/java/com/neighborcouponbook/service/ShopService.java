package com.neighborcouponbook.service;

import com.neighborcouponbook.model.QShop;
import com.neighborcouponbook.model.vo.ShopVo;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ShopService {

    ResponseEntity<?> createShop(ShopVo shopVo);
    ResponseEntity<?> updateShop(Long shopId, ShopVo shopVo);

    ResponseEntity<?> deleteShop(Long shopId, ShopVo shopVo);

    //회원의 사업장 리스트
    List<ShopVo> selectShopList(Long userId);
    //사업장 세부정보 반환
    JPAQuery<QShop> selectShopInfo(Long shopId);
}
