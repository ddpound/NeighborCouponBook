package com.neighborcouponbook.service.impl;

import com.neighborcouponbook.common.response.FileUploadResponse;
import com.neighborcouponbook.common.response.ResponseMetaData;
import com.neighborcouponbook.common.response.ResponseUtil;
import com.neighborcouponbook.common.util.AuthUtil;
import com.neighborcouponbook.model.*;
import com.neighborcouponbook.model.search.ShopSearch;
import com.neighborcouponbook.model.search.ShopTypeSearch;
import com.neighborcouponbook.model.vo.CouponBookFileVo;
import com.neighborcouponbook.model.vo.CouponUserVo;
import com.neighborcouponbook.model.vo.ShopTypeVo;
import com.neighborcouponbook.model.vo.ShopVo;
import com.neighborcouponbook.repository.CouponUserRepository;
import com.neighborcouponbook.repository.ShopRepository;
import com.neighborcouponbook.repository.ShopTypeRepository;
import com.neighborcouponbook.service.CouponBookFileService;
import com.neighborcouponbook.service.CouponUserService;
import com.neighborcouponbook.service.ShopService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.neighborcouponbook.model.QShop.shop;

@Log4j2
@RequiredArgsConstructor
@Service
public class ShopServiceImpl implements ShopService {
    private final JPAQueryFactory queryFactory;
    private final ShopRepository shopRepository;
    private final CouponUserRepository couponUserRepository;
    private final ShopTypeRepository shopTypeRepository;
    private final CouponUserService couponUserService;
    private final CouponBookFileService couponBookFileService;

    @Override
    @Transactional
    public ResponseEntity<?> createShop(ShopVo shopVo , MultipartFile file) {
        try {
            if (shopVo == null) {
                return ResponseUtil.createSuccessResponse(-1, "입력 데이터가 없습니다.");
            }

            CouponUser couponUser = couponUserRepository.findById(AuthUtil.getLoginUserData().getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("유효한 회원정보가 없습니다."));
            ShopType shopType = shopTypeRepository.findById(shopVo.getShopTypeId())
                    .orElseThrow(() -> new IllegalArgumentException("유효한 업종 정보가 없습니다."));

            CouponBookFile couponBookFile = null;

            if(file != null && !file.isEmpty()){
                List<MultipartFile> files = new ArrayList<>();
                files.add(file);
                FileUploadResponse fileResponse = couponBookFileService.uploadFiles(files);

                if(fileResponse != null && fileResponse.getFiles() != null && !fileResponse.getFiles().isEmpty()){
                    // file data 는 무조건 하나
                    couponBookFile = couponBookFileService.selectCouponBookFile(fileResponse.getFiles().get(0).getFileId());
                }
            }

            Shop shop = new Shop();
            shop.createShop(couponUser,
                    shopType,
                    couponBookFile,
                    shopVo.getShopName(),
                    shopVo.getShopAddress(),
                    shopVo.getBusinessRegistrationNumber(),
                    shopVo.getShopDescription());
            shop.settingCreateData(AuthUtil.getLoginUserData().getUserId());

            ShopVo returnShopVo = ShopVo.builder().build().convertToShopVo(shopRepository.save(shop));

            /**
             *  초기 생성이니 유저 타입도 같이 진행
             *  TODO : 해당 메소드 내부에 한번더 select가 진행되어 2중 select임 updateCouponUserType 메소드의 로직을 분리할 필요가 있음.
             * */
            CouponUserVo couponUserVo = new CouponUserVo().convertToVo(couponUser);
            couponUserVo.setCouponUserType(CouponUser.CouponUserType.SHOPOWNER);
            couponUserService.updateCouponUserType(couponUserVo);

            return ResponseUtil.createResponse(returnShopVo,
                    ResponseMetaData.builder()
                            .dataDescription("가게 등록 성공 데이터 입니다.")
                            .dataTotalCount(1L)
                            .build(),
                    1 ,
                    "저장이 완료되었습니다.",
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return ResponseUtil.createSuccessResponse(-1, "shop 저장에 실패했습니다. : " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> createShopType(ShopVo shopVo) {
        try {
            ShopType shopType = new ShopType();
            shopType.createShopType(shopVo.getShopTypeName());
            shopType.settingCreateData(AuthUtil.getLoginUserData().getUserId());

            shopTypeRepository.save(shopType);

            return ResponseUtil.createSuccessResponse(1, "저장이 완료되었습니다.");
        } catch (Exception e) {
            return ResponseUtil.createSuccessResponse(-1, "저장에 실패했습니다. : " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<?> selectShopTypeList(ShopTypeSearch search){
        try {
            QShopType qShopType = QShopType.shopType;
            List<ShopType> selectQShopTypeList =
                    queryFactory
                            .select(qShopType)
                            .from(qShopType)
                            .fetch();

            List<ShopTypeVo> resultShopTypeList = ShopTypeVo.builder().build().converToShopTypeVoList(selectQShopTypeList);

            ResponseMetaData responseMetaData = ResponseMetaData
                    .builder()
                    .dataTotalCount((long) selectQShopTypeList.size())
                    .dataDescription("카페 타입에 관한 데이터입니다.")
                    .build();

            return ResponseUtil.createResponse(resultShopTypeList, responseMetaData, 1,"카페 타입 반환에 성공했습니다", HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.createSuccessResponse(-1, "반환에 실패했습니다 : " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateShop(ShopVo shopVo, MultipartFile file) {
        try {
            Shop updatedShopInfo = shopRepository.findById(shopVo.getShopId())
                    .orElseThrow(() -> new IllegalArgumentException("상점 정보가 존재하지 않습니다."));

            CouponBookFile couponBookFile = null;

            if(file != null && !file.isEmpty()){
                List<MultipartFile> files = new ArrayList<>();
                files.add(file);
                FileUploadResponse fileResponse = couponBookFileService.uploadFiles(files);

                if(fileResponse != null && fileResponse.getFiles() != null && !fileResponse.getFiles().isEmpty()){
                    // file data 는 무조건 하나
                    couponBookFile = couponBookFileService.selectCouponBookFile(fileResponse.getFiles().get(0).getFileId());
                }
            }

            updatedShopInfo.updateShop(shopVo.getShopAddress(),
                    shopVo.getBusinessRegistrationNumber(),
                    shopVo.getShopDescription(),
                    couponBookFile);

            updatedShopInfo.updateData(AuthUtil.getLoginUserData().getUserId());

            // 트랜잭션이 끝날 때 변경감지로 update2
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
    public List<ShopVo> selectShopList(ShopSearch search) {
        try {
            QShop shop = QShop.shop;
            QShopType shopType = QShopType.shopType;
            QCouponUser user = QCouponUser.couponUser;

            List<Shop> results = queryFactory
                    .select(shop)
                    .from(shop)
                    .join(shop.shopType, shopType).fetchJoin()
                    .join(shop.couponUser, user).fetchJoin()
                    .where(shop.isDeleted.eq(false).and(
                            // 다양한 검색 조건에 따라 동적 조건 생성
                            createSearchCondition(search)
                            )
                    )
                    .fetch();

            // 조회 결과가 없으면 null 반환 또는 예외 처리
            if (results == null || results.isEmpty()) {
                throw new RuntimeException("등록한 상점 정보가 없습니다.");
            }

            List<ShopVo> resultList = new ArrayList<>();
            // 컨버트용 vo
            ShopVo convertVo = new ShopVo();
            for (Shop shopItem : results) {
                log.info(shopItem.toString());
                resultList.add(convertVo.convertToShopVo(Objects.requireNonNull(shopItem)));
            }

            return resultList;

        } catch (Exception e) {
            log.error("selectShopList ERROR : {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private BooleanBuilder createSearchCondition(ShopSearch search) {
        BooleanBuilder builder = new BooleanBuilder();

        if (search.getUserId() != null) {
            builder.and(shop.couponUser.userId.eq(search.getUserId()));
        }
        if (search.getShopId() != null) {
            builder.and(shop.shopId.eq(search.getShopId()));
        }
        if (search.getShopName() != null) {
            builder.and(shop.shopName.like("%" + search.getShopName() + "%"));
        }
        return builder;
    }

    @Override
    @Transactional(readOnly = true)
    public ShopVo selectShopInfo(ShopSearch search) {
        QShop shop = QShop.shop;
        QShopType shopType = QShopType.shopType;
        QCouponUser user = QCouponUser.couponUser;

        Tuple shopVo = queryFactory
                .select(shop.shopId,
                        shop.couponUser.userId,
                        user.userName,
                        shop.shopType.shopTypeId,
                        shopType.shopTypeName,
                        shop.shopName,
                        shop.shopAddress,
                        shop.businessRegistrationNumber,
                        shop.shopDescription,
                        shop.shopThumbnail)
                .from(shop)
                .join(shop.shopType, shopType).on(shopType.isDeleted.eq(false))
                .join(shop.couponUser, user).on(user.isDeleted.eq(false))
                .where(
                        shop.shopId.eq(search.getShopId())
                                .and(shop.isDeleted.eq(false))
                )
                .fetchOne();

        // 조회 결과가 없으면 null 반환 또는 예외 처리
        if (shopVo == null) {
            throw new RuntimeException("유효한 상점 정보가 아닙니다.");
        }

        return new ShopVo(
                shopVo.get(shop.shopId),
                shopVo.get(shop.couponUser.userId),
                shopVo.get(user.userName),
                shopVo.get(shop.shopType.shopTypeId),
                shopVo.get(shopType.shopTypeName),
                shopVo.get(shop.shopName),
                shopVo.get(shop.shopAddress),
                shopVo.get(shop.businessRegistrationNumber),
                shopVo.get(shop.shopDescription),
                shopVo.get(shop.shopThumbnail) != null ? new CouponBookFileVo().convertToVo(shopVo.get(shop.shopThumbnail)) : null,
                shopVo.get(shop.couponUser) != null ? new CouponUserVo().convertToVo(shopVo.get(shop.couponUser)) : null
                );
    }
}
