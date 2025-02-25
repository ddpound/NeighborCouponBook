package com.neighborcouponbook.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 쿠폰 테이블
 * */
@SuperBuilder
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Entity
public class Coupon extends CommonColumn{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponId;

    private Long shopId;

    private Integer goalsNumber;

    private String couponDescription;
}
