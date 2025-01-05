package com.neighborcouponbook.model.vo;

import com.neighborcouponbook.common.util.NullChecker;
import com.neighborcouponbook.model.Shop;
import com.querydsl.core.Tuple;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ShopVo extends CommonColumnVo{
    private Long shopId;
    private Long userId; //shopOwnerId
    private String shopOwnerName;
    private Long shopTypeId;
    private String shopTypeName;
    private String shopName;
    private String shopAddress;
    private String businessRegistrationNumber;
    private String shopDescription;
    private CouponBookFileVo couponBookFileVo;

    public ShopVo convertToShopVo(Shop shop) {
        ShopVo shopVo = new ShopVo();

        if(!NullChecker.isNull(shop.getShopId())) shopVo.setShopId(shop.getShopId());
        if(!NullChecker.isNullString(shop.getShopName())) shopVo.setShopName(shop.getShopName());
        if(!NullChecker.isNullString(shop.getShopAddress())) shopVo.setShopAddress(shop.getShopAddress());
        if(!NullChecker.isNullString(shop.getBusinessRegistrationNumber())) shopVo.setBusinessRegistrationNumber(shop.getBusinessRegistrationNumber());
        if(!NullChecker.isNullString(shop.getShopDescription())) shopVo.setShopDescription(shop.getShopDescription());

        if(!NullChecker.isNull(shop.returnAllCommonColumn())) shopVo.settingCommonColumnVo(shop.returnAllCommonColumn());

        return shopVo;
    }

    public List<ShopVo> convertToShopVoList(List<Shop> shops) {
        List<ShopVo> shopVoList = new ArrayList<>();
        shops.forEach(shop -> shopVoList.add(convertToShopVo(shop)));

        return shopVoList;
    }
}
