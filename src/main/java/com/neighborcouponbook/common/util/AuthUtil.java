package com.neighborcouponbook.common.util;

import com.neighborcouponbook.config.auth.PrincipalDetail;
import com.neighborcouponbook.model.vo.CouponUserVo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtil {

    public static CouponUserVo getLoginUserData(){

        if(SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null){
            // 현재 인증된 사용자 정보 가져오기
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


            // 인증 객체에서 Principal 객체 가져오기
            PrincipalDetail principalDetail = (PrincipalDetail) authentication.getPrincipal();

            // 필요한 사용자 정보 사용
            String username = principalDetail.getUsername();
            Long userId = principalDetail.getCouponUserId();

            return CouponUserVo.builder().userId(userId).userName(username).build();
        }else{
            return null;
        }
    }

    public static Long getLoginUserId() {
        if(getLoginUserData() != null) getLoginUserData().getUserId();
        return null;
    }
}
