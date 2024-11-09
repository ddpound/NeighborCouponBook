package com.neighborcouponbook.service;


import com.neighborcouponbook.model.Role;
import com.neighborcouponbook.model.search.RoleSearch;
import com.neighborcouponbook.model.vo.RoleVo;

import java.util.List;

public interface RoleService {

    List<RoleVo> selectRoleList(RoleSearch search);

    int saveRole(Role role);

    int updateRole(Role role);

    int softDeleteRole(Long id);
}
