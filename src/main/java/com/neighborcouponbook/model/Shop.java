package com.neighborcouponbook.model;

import com.neighborcouponbook.common.util.NullChecker;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Shop extends CommonColumn{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long shopId;

    @Column(nullable = false)
    private String shopName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private CouponUser couponUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_type_id")
    private ShopType shopType;

    private String shopAddress;
    private String businessRegistrationNumber;
    private String shopDescription;

    public void createShop(CouponUser couponUser, ShopType shopType, String shopName, String shopAddress, String businessRegistrationNumber, String shopDescription) {

        if(!NullChecker.isNull(couponUser)) this.couponUser = couponUser;
        if(!NullChecker.isNull(shopType)) this.shopType = shopType;
        this.shopName = shopName;
        this.shopAddress = shopAddress;
        this.businessRegistrationNumber = businessRegistrationNumber;
        this.shopDescription = shopDescription;
    }
}
