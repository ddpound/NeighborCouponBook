package com.neighborcouponbook.model.vo;

import com.neighborcouponbook.common.util.NullChecker;
import com.neighborcouponbook.model.CouponUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CouponUserVo extends CommonColumnVo {

    private Long userId;
    private String userLoginId;
    private String userName;
    private String password;
    private String token;
    private CouponUser.CouponUserType couponUserType;

    public CouponUserVo convertToVo(CouponUser couponUser) {
        CouponUserVo couponUserVo = new CouponUserVo();

        if(!NullChecker.isNull(couponUser.getUserId())) couponUserVo.setUserId(couponUser.getUserId());
        if(!NullChecker.isNullString(couponUser.getUserLoginId())) couponUserVo.setUserLoginId(couponUser.getUserLoginId());
        if(!NullChecker.isNullString(couponUser.getUserName())) couponUserVo.setUserName(couponUser.getUserName());

        if(!NullChecker.isNull(couponUser.returnAllCommonColumn())) couponUserVo.settingCommonColumnVo(couponUser.returnAllCommonColumn());

        // password를 반환받을 경우는 거의없음.
        //if(!NullChecker.isNullString(couponUser.getPassword())) couponUserDto.setPassword(couponUser.getPassword());

        return couponUserVo;
    }

    public List<CouponUserVo> convertDtoToList(List<CouponUser> couponUserList) {
        List<CouponUserVo> couponUserDtoList = new ArrayList<CouponUserVo>();

        couponUserList.forEach(couponUser -> {
            couponUserDtoList.add(convertToVo(couponUser));
        });

        return couponUserDtoList;
    }

}
