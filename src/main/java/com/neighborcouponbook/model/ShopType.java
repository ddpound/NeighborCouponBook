package com.neighborcouponbook.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
* 업종 코드 테이블
 * 카페, 음식점 등 등록 예정
* */
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class ShopType extends CommonColumn{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long shopTypeId;

    private String shopTypeName;

    public void createShopType(String shopTypeName) {
        this.shopTypeName = shopTypeName;
    }
}
