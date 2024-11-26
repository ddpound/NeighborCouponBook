package com.neighborcouponbook.service.impl;

import com.neighborcouponbook.common.response.ResponseUtil;
import com.neighborcouponbook.common.util.AuthUtil;
import com.neighborcouponbook.model.CouponUser;
import com.neighborcouponbook.model.QShop;
import com.neighborcouponbook.model.Shop;
import com.neighborcouponbook.model.ShopType;
import com.neighborcouponbook.model.vo.ShopVo;
import com.neighborcouponbook.repository.CouponUserRepository;
import com.neighborcouponbook.repository.ShopRepository;
import com.neighborcouponbook.repository.ShopTypeRepository;
import com.neighborcouponbook.service.ShopService;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

            CouponUser couponUser = couponUserRepository.findById(AuthUtil.getLoginUserData().getUserId())
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
            shop.settingCreateData(AuthUtil.getLoginUserData().getUserId());

            shopRepository.save(shop);

            return ResponseUtil.createSuccessResponse(1, "shop [" + shopVo.getShopName() + "] 저장이 완료되었습니다.");
        } catch (Exception e) {
            return ResponseUtil.createSuccessResponse(-1, "shop 저장에 실패했습니다. : " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateShop(Long id, ShopVo shopVo) {
        try {
            Shop updatedShopInfo = shopRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("상점 정보가 존재하지 않습니다."));

            updatedShopInfo.updateShop(shopVo.getShopAddress(), shopVo.getBusinessRegistrationNumber(), shopVo.getShopDescription());
            updatedShopInfo.updateData(AuthUtil.getLoginUserData().getUserId());

            //트랜잭션이 끝날 때 변경감지로 update
            return ResponseUtil.createSuccessResponse(1, "수정이 완료되었습니다.");
        } catch (Exception e) {
            log.error("Shop update failed : ", e);
            return ResponseUtil.createSuccessResponse(-1, "수정에 실패했습니다. : " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteShop(Long id, ShopVo shopVo) {
        try {
            Shop softDeleteShopInfo = shopRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("상점 정보가 존재하지 않습니다"));
            softDeleteShopInfo.softDeleteData(AuthUtil.getLoginUserData().getUserId());

            return ResponseUtil.createSuccessResponse(1, "삭제가 완료되었습니다.");
        } catch (Exception e) {
            return ResponseUtil.createSuccessResponse(-1, "삭제에 실패했습니다. : " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShopVo> selectShopList(Long userId) {
        try {
            QShop shop = QShop.shop;

            List<Shop> list = queryFactory
                    .select(shop)
                    .from(shop)
                    .join(shop.shopType).fetchJoin()
                    .where( shop.couponUser.userId.eq(userId)
                            .and(shop.isDeleted.eq(false))
                    )
                    .fetch();

            ShopVo vo = new ShopVo();
            List<ShopVo> voList = vo.convertToShopVoList(list);
            //수정필요. type 안나온다
            return voList;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JPAQuery<QShop> selectShopInfo(Long shopId) {
        return null;
    }
}
