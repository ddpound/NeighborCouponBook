package com.neighborcouponbook.model.vo;

import com.neighborcouponbook.model.CouponUser;
import com.neighborcouponbook.model.ShopType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ShopVo {
    private Long shopId;
    private Long userId;
    private Long shopTypeId;
    private String shopName;
    private String shopAddress;
    private String businessRegistrationNumber;
    private String shopDescription;
}
