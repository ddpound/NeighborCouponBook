package com.neighborcouponbook.model.vo;

import com.neighborcouponbook.common.util.NullChecker;
import com.neighborcouponbook.model.ShopType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ShopTypeVo extends CommonColumnVo{
    private long shopTypeId;
    private String shopTypeName;

    public ShopTypeVo converToShopTypeVo(ShopType shopType) {
        ShopTypeVo shopTypeVo = new ShopTypeVo();

        if(!NullChecker.isNull(shopType.getShopTypeId())) shopTypeVo.setShopTypeId(shopType.getShopTypeId());
        if(!NullChecker.isNullString(shopType.getShopTypeName())) shopTypeVo.setShopTypeName(shopType.getShopTypeName());

        return shopTypeVo;
    }

    public List<ShopTypeVo> converToShopTypeVoList(List<ShopType> shopTypes) {
        List<ShopTypeVo> shopTypeVoList = new ArrayList<>();
        shopTypes.forEach(shopType -> {shopTypeVoList.add(converToShopTypeVo(shopType));});
        return shopTypeVoList;
    }

}
