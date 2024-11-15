package com.neighborcouponbook.service;

import com.neighborcouponbook.model.Menu;
import com.neighborcouponbook.model.search.MenuSearch;
import com.neighborcouponbook.model.vo.MenuVo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MenuService {

    List<MenuVo> selectMenuList(MenuSearch menuSearch);

    ResponseEntity<?> createMenu(Menu menu);

    ResponseEntity<?> createMenu(MenuVo menuVo);

    ResponseEntity<?> updateMenu(Menu menu);

    Integer softDelteMenu(Menu menu);

    BooleanBuilder settingMenuSearchBuilder(MenuSearch menuSearch);

    JPAQuery<Menu> selectMenuListQuery(MenuSearch menuSearch);

}
