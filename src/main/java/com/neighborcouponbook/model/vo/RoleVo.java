package com.neighborcouponbook.model.vo;

import com.neighborcouponbook.common.util.NullChecker;
import com.neighborcouponbook.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoleVo {

    private Long roleId;
    private String roleName;

    public RoleVo convertToRoleVo(Role role) {
        RoleVo roleVo = new RoleVo();

        if(!NullChecker.isNull(role.getRoleId())) roleVo.setRoleId(role.getRoleId());
        if(!NullChecker.isNullString(role.getRoleName())) roleVo.setRoleName(role.getRoleName());

        return roleVo;
    }

    public List<RoleVo> convertToRoleVoList(List<Role> roleList) {

        List<RoleVo> roleVoList = new ArrayList<>();

        roleList.forEach(role -> {
            roleVoList.add(convertToRoleVo(role));
        });

        return roleVoList;
    }

}
