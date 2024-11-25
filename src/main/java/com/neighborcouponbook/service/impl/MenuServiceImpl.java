package com.neighborcouponbook.service.impl;

import com.neighborcouponbook.common.response.ResponseMetaData;
import com.neighborcouponbook.common.response.ResponseUtil;
import com.neighborcouponbook.common.util.AuthUtil;
import com.neighborcouponbook.common.util.NullChecker;
import com.neighborcouponbook.model.Menu;
import com.neighborcouponbook.model.QMenu;
import com.neighborcouponbook.model.search.CommonSearch;
import com.neighborcouponbook.model.search.MenuSearch;
import com.neighborcouponbook.model.vo.MenuVo;
import com.neighborcouponbook.repository.MenuRepository;
import com.neighborcouponbook.service.MenuService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;

    private final JPAQueryFactory queryFactory;

    @Transactional(readOnly = true)
    @Override
    public List<MenuVo> selectMenuList(MenuSearch menuSearch) {

        List<Menu> resultList = selectMenuListQuery(menuSearch).fetch();

        MenuVo menuVo = new MenuVo();
        return menuVo.convertToMenuVoList(resultList);
    }

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<?> responseSelectMenuList(MenuSearch menuSearch) {

        List<Menu> resultList = selectMenuListQuery(menuSearch).fetch();

        MenuVo menuVo = new MenuVo();
        List<MenuVo> menuVoList = menuVo.convertToMenuVoList(resultList);
        Long menuListTotalCount = selectMenuTotalCount(menuSearch);

        return ResponseUtil
                .createResponse(
                        menuVoList,
                        ResponseMetaData.builder().dataTotalCount(menuListTotalCount).build(),
                        1,
                        "메뉴 리스트 반환에 성공했습니다.",
                        HttpStatus.OK);
    }

    @Override
    public Long selectMenuTotalCount(MenuSearch menuSearch) {
        return selectMenuListCountQuery(menuSearch).fetchOne();
    }

    @Transactional
    @Override
    public ResponseEntity<?> createMenu(Menu menu) {
        try {
            menuRepository.save(menu);

            return ResponseUtil.createSuccessResponse(1,"메뉴 저장이 완료되었습니다.");
        } catch (ConstraintViolationException e) {
            return ResponseUtil.createSuccessResponse(-1,"동일한 데이터를 입력했습니다.");
        } catch (Exception e){
            return ResponseUtil.createSuccessResponse(-1,"메뉴 저장이 실패했습니다. : " + e.getMessage());
        }
    }

    @Transactional
    @Override
    public ResponseEntity<?> createMenu(MenuVo menuVo) {
       try {
           if(menuVo == null || menuVo.getMenuName() == null || menuVo.getMenuUri() == null)
               return ResponseUtil.createSuccessResponse(-1,"메뉴 이름이나 uri 가 비어있습니다. ");

           Menu createMenu = new Menu();
           createMenu.createMenu(menuVo.getMenuUri(), menuVo.getMenuName(), menuVo.getParentMenuId());

           /** todo null 방지 코드 추가할 것. */
           createMenu.settingCreateData(AuthUtil.getLoginUserData().getUserId());

           menuRepository.save(createMenu);

           return ResponseUtil.createSuccessResponse(1 , "메뉴 저장이 완료 되었습니다");
       }catch (Exception e){
           return ResponseUtil.createSuccessResponse(-1,"메뉴 저장이 실패했습니다. : " + e.getMessage());
       }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseEntity<?> createMenu(List<MenuVo> menuVoList) {

        try {
            if(menuVoList != null && !menuVoList.isEmpty()){
                List<Menu> menus = new ArrayList<>();

                for(MenuVo menuVo : menuVoList){
                    if(menuVo == null || menuVo.getMenuName() == null || menuVo.getMenuUri() == null)
                        return ResponseUtil.createSuccessResponse(-1,"메뉴 이름이나 uri 가 비어있습니다. ");

                    Menu createMenu = new Menu();
                    createMenu.createMenu(menuVo.getMenuUri(), menuVo.getMenuName(), menuVo.getParentMenuId());

                    /** todo null 방지 코드 추가할 것. */
                    createMenu.settingCreateData(AuthUtil.getLoginUserData().getUserId());

                    menus.add(createMenu);
                }

                menuRepository.saveAll(menus);
            }else{
                throw new Exception();
            }

            return ResponseUtil.createSuccessResponse(1 , "메뉴 저장이 완료 되었습니다");
        }catch (Exception e){
            return ResponseUtil.createSuccessResponse(-1,"메뉴 저장이 실패했습니다. : " + e.getMessage());
        }
    }

    @Transactional
    @Override
    public ResponseEntity<?> updateMenu(Menu menu) {
        try {
            if(menu == null || (menu.getMenuName() == null && menu.getMenuUri() == null))
                return ResponseUtil.createSuccessResponse(-1,"수정 값 하나라도 입력해주셔야 합니다. ");

            Optional<Menu> updateMenu = menuRepository.findById(menu.getMenuId());
            updateMenu.ifPresent(value -> value.updateMenu(menu));
            return ResponseUtil.createSuccessResponse(1 , "메뉴 수정이 완료 되었습니다.");
        }catch (Exception e){
            return ResponseUtil.createSuccessResponse(-1, "메뉴 업데이트에 실패했습니다.: " + e.getMessage());
        }
    }

    @Transactional
    @Override
    public Integer softDelteMenu(Menu menu) {
        try {
            menu.softDeleteData(AuthUtil.getLoginUserData().getUserId());
            return 1;
        }catch (Exception e){
            return -1;
        }
    }

    @Override
    public BooleanBuilder settingMenuSearchBuilder(MenuSearch menuSearch) {
        BooleanBuilder builder = new BooleanBuilder();

        if(!NullChecker.isNull(menuSearch.getMenuId())) builder.and(QMenu.menu.menuId.eq(menuSearch.getMenuId()));
        if(!NullChecker.isNullString(menuSearch.getMenuUri())) builder.and(QMenu.menu.menuUri.eq(menuSearch.getMenuUri()));
        if(!NullChecker.isNullString(menuSearch.getMenuName())) builder.and(QMenu.menu.menuName.eq(menuSearch.getMenuName()));

        builder.and(QMenu.menu.isDeleted.eq(false));

        return builder;
    }

    @Override
    public JPAQuery<Menu> selectMenuListQuery(MenuSearch menuSearch) {
        QMenu menu = QMenu.menu;

        JPAQuery<Menu> returnJpaQuery = queryFactory
                .select(menu)
                .from(menu)
                .where(settingMenuSearchBuilder(menuSearch))
                .offset(menuSearch.getOffset())
                .limit(menuSearch.getPageSize());

        // 정렬 조건 추가
        returnJpaQuery = applySorting(returnJpaQuery, menuSearch, menu);

        return returnJpaQuery;
    }

    private JPAQuery<Menu> applySorting(JPAQuery<Menu> query, MenuSearch menuSearch, QMenu menu) {
        if (menuSearch.getSort() == null || menuSearch.getSortOrder() == null) {
            return query; // 정렬 조건이 없으면 그대로 반환
        }

        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(menuSearch, menu);
        if (orderSpecifier != null) {
            query = query.orderBy(orderSpecifier);
        }

        return query;
    }

    /** todo 정렬은 나중에 복수로 받을 수 있음. 수정요망. */
    private OrderSpecifier<?> getOrderSpecifier(MenuSearch menuSearch, QMenu menu) {
        return switch (menuSearch.getSort()) {
            case menuId -> menuSearch.getSortOrder().equals(CommonSearch.OrderBy.asc)
                    ? menu.menuId.asc() : menu.menuId.desc();
            case menuName -> menuSearch.getSortOrder().equals(CommonSearch.OrderBy.asc)
                    ? menu.menuName.asc() : menu.menuName.desc();
            case menuUri -> menuSearch.getSortOrder().equals(CommonSearch.OrderBy.asc)
                    ? menu.menuUri.asc() : menu.menuUri.desc();
            default -> null; // 예외 시 null 반환
        };
    }

    @Override
    public JPAQuery<Long> selectMenuListCountQuery(MenuSearch menuSearch) {
        QMenu menu = QMenu.menu;

        return queryFactory
                .select(menu.count())
                .from(menu)
                .where(settingMenuSearchBuilder(menuSearch));
    }
}
