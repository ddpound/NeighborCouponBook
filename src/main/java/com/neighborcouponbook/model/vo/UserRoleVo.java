package com.neighborcouponbook.model.vo;

import com.neighborcouponbook.common.util.NullChecker;
import com.neighborcouponbook.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserRoleVo {

    private Long userRoleId;
    private Long userId;
    private Long roleId;

    public UserRoleVo convertToUserRoleVo(UserRole userRole) {

        UserRoleVo userRoleVo = new UserRoleVo();

        if(!NullChecker.isNull(userRole.getUserRoleId())) userRoleVo.setUserRoleId(userRole.getUserRoleId());
        if(!NullChecker.isNull(userRole.getUserId())) userRoleVo.setUserId(userRole.getUserId());
        if(!NullChecker.isNull(userRole.getRoleId())) userRoleVo.setRoleId(userRole.getRoleId());

        return userRoleVo;
    }

    public List<UserRoleVo> convertToUserRoleVoList(List<UserRole> userRoleList) {
        List<UserRoleVo> userRoleVoList = new ArrayList<>();

        userRoleList.forEach(userRole -> {
            userRoleVoList.add(convertToUserRoleVo(userRole));
        });

        return userRoleVoList;
    }
}
