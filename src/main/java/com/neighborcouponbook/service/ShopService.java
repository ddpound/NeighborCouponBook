package com.neighborcouponbook.service;

import com.neighborcouponbook.model.QShop;
import com.neighborcouponbook.model.search.ShopSearch;
import com.neighborcouponbook.model.search.ShopTypeSearch;
import com.neighborcouponbook.model.vo.ShopTypeVo;
import com.neighborcouponbook.model.vo.ShopVo;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
/*
* ShopType도 여기서 같이 관리
* */
public interface ShopService {

    ResponseEntity<?> createShop(ShopVo shopVo, MultipartFile file);

    ResponseEntity<?> createShopType(ShopVo shopVo);
    ResponseEntity<?> updateShop(ShopVo shopVo);

    ResponseEntity<?> deleteShop(Long shopId, ShopVo shopVo);

    List<ShopVo> selectShopList(ShopSearch search);

    ShopVo selectShopInfo(ShopSearch search);

    ResponseEntity<?> selectShopTypeList(ShopTypeSearch search);

}
