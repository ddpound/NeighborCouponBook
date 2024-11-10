package com.neighborcouponbook.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * user와 user 에 관한 데이터
 * */
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CouponUserWithUserRole {
    private CouponUserVo couponUserVo;
    private UserRoleVo userRoleVo;
}
