package com.neighborcouponbook.service;


import com.neighborcouponbook.model.UserRole;
import com.neighborcouponbook.model.search.UserRoleSearch;
import com.neighborcouponbook.model.vo.UserRoleVo;

import java.util.List;

public interface UserRoleService {

    List<UserRoleVo> selectUserRoleList(UserRoleSearch search);

    UserRole selectUserRole(Long userRoleId);

    Integer saveUserRole(UserRole userRole);

}
