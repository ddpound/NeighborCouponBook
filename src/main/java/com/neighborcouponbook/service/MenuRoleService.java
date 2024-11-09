package com.neighborcouponbook.service;

import com.neighborcouponbook.model.MenuRole;
import com.neighborcouponbook.model.search.MenuRoleSearch;
import com.neighborcouponbook.model.vo.MenuRoleVo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MenuRoleService {

    ResponseEntity<?> saveMenuRole(MenuRoleVo menuRoleVo);

    ResponseEntity<?> saveMenuRoles(List<MenuRoleVo> menuRoleVos);

    ResponseEntity<?> updateMenuRole(MenuRoleVo menuRoleVo);

    ResponseEntity<?> updateMenuRoles(List<MenuRoleVo> menuRoleVos);

    ResponseEntity<?> softDeleteMenuRole(Long menuRoleId);

    MenuRoleVo selectMenuRole(MenuRoleSearch menuRoleSearch);

    List<MenuRoleVo> selectMenuRoleVoList(MenuRoleSearch menuRoleSearch);

    List<MenuRole> selectMenuRoleList(MenuRoleSearch menuRoleSearch);

    BooleanBuilder settingMenuRoleBuilder(MenuRoleSearch menuRoleSearch);

    public JPAQuery<MenuRole> selectMenuRoleQuery(MenuRoleSearch menuRoleSearch);
}
