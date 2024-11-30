package com.neighborcouponbook.service;

import com.neighborcouponbook.model.Menu;
import com.neighborcouponbook.model.search.MenuSearch;
import com.neighborcouponbook.model.vo.MenuVo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MenuService {

    /** menu 권한을 위한 모든 메뉴 select, DB 접근 회수를 낮추기 위해 캐싱작업이 필요*/
    List<MenuVo> selectAllMenus();

    List<MenuVo> selectMenuList(MenuSearch menuSearch);

    ResponseEntity<?> responseSelectMenuList(MenuSearch menuSearch);

    /** 메뉴 리스트 카운트 */
    Long selectMenuTotalCount(MenuSearch menuSearch);

    ResponseEntity<?> createMenu(Menu menu);

    ResponseEntity<?> createMenu(MenuVo menuVo);

    ResponseEntity<?> createMenu(List<MenuVo> menuVoList);

    ResponseEntity<?> updateMenu(Menu menu);

    Integer softDelteMenu(Menu menu);

    BooleanBuilder settingMenuSearchBuilder(MenuSearch menuSearch);

    JPAQuery<Menu> selectMenuListQuery(MenuSearch menuSearch);

    JPAQuery<Long> selectMenuListCountQuery(MenuSearch menuSearch);

}
