package com.neighborcouponbook.service.impl;

import com.neighborcouponbook.common.response.ResponseUtil;
import com.neighborcouponbook.model.CouponUser;
import com.neighborcouponbook.model.Shop;
import com.neighborcouponbook.model.ShopType;
import com.neighborcouponbook.model.vo.ShopVo;
import com.neighborcouponbook.repository.CouponUserRepository;
import com.neighborcouponbook.repository.ShopRepository;
import com.neighborcouponbook.repository.ShopTypeRepository;
import com.neighborcouponbook.service.ShopService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@RequiredArgsConstructor
@Service
public class ShopServiceImpl implements ShopService {
    private final JPAQueryFactory queryFactory;
    private final ShopRepository shopRepository;
    private final CouponUserRepository couponUserRepository;
    private final ShopTypeRepository shopTypeRepository;
    @Override
    @Transactional
    public ResponseEntity<?> createShop(ShopVo shopVo) {
        try {
            if (shopVo == null) {
                return ResponseUtil.createSuccessResponse(-1, "입력 데이터가 없습니다.");
            }

            CouponUser couponUser = couponUserRepository.findById(shopVo.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("유효한 회원정보가 없습니다."));
            ShopType shopType = shopTypeRepository.findById(shopVo.getShopTypeId())
                    .orElseThrow(() -> new IllegalArgumentException("유효한 업종 정보가 없습니다."));

            Shop shop = new Shop();
            shop.createShop(couponUser,
                    shopType,
                    shopVo.getShopName(),
                    shopVo.getShopAddress(),
                    shopVo.getBusinessRegistrationNumber(),
                    shopVo.getShopDescription());

            shopRepository.save(shop);

            return ResponseUtil.createSuccessResponse(1, "shop [" + shopVo.getShopName() + "] 저장이 완료되었습니다.");
        } catch (Exception e) {
            return ResponseUtil.createSuccessResponse(-1, "shop 저장에 실패했습니다. : " + e.getMessage());
        }
    }
}
