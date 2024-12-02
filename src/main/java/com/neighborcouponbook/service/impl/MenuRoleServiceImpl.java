package com.neighborcouponbook.service.impl;


import com.neighborcouponbook.common.response.ApiCommonResponse;
import com.neighborcouponbook.common.response.ResponseUtil;
import com.neighborcouponbook.common.util.AuthUtil;
import com.neighborcouponbook.common.util.NullChecker;
import com.neighborcouponbook.model.MenuRole;
import com.neighborcouponbook.model.QMenuRole;
import com.neighborcouponbook.model.search.MenuRoleSearch;
import com.neighborcouponbook.model.vo.MenuRoleVo;
import com.neighborcouponbook.repository.MenuRoleRepository;
import com.neighborcouponbook.service.MenuRoleService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Log4j2
@RequiredArgsConstructor
@Service
public class MenuRoleServiceImpl implements MenuRoleService {

    private final JPAQueryFactory queryFactory;

    private final MenuRoleRepository menuRoleRepository;

    @Transactional
    @Override
    public ResponseEntity<ApiCommonResponse<String>> saveMenuRole(MenuRoleVo menuRoleVo) {
        try {
            MenuRole saveMenuRole = new MenuRole();

            saveMenuRole.createMenuRole(menuRoleVo);
            saveMenuRole.settingCreateData(AuthUtil.getLoginUserId() != null ? AuthUtil.getLoginUserId() : 1L);

            menuRoleRepository.save(saveMenuRole);
            return ResponseUtil.createResponse(null, 1, "저장에 성공했습니다.", HttpStatus.OK);
        }catch (Exception e) {
            log.error(e);
            return ResponseUtil.createResponse(null, -1, "저장에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @Override
    public ResponseEntity<?> saveMenuRoles(List<MenuRoleVo> menuRoleVos) {
        try {
            List<MenuRole> saveMenuRoleList = new ArrayList<>();
            for (MenuRoleVo menuRoleVo : menuRoleVos) {
                MenuRole saveMenuRole = new MenuRole();

                saveMenuRole.createMenuRole(menuRoleVo);
                saveMenuRole.settingCreateData(AuthUtil.getLoginUserData().getUserId());

                saveMenuRoleList.add(saveMenuRole);
            }

            menuRoleRepository.saveAll(saveMenuRoleList);
            return ResponseUtil.createSuccessResponse(1,  "저장에 성공했습니다.");
        }catch (Exception e) {
            log.error(e);
            return ResponseUtil.createSuccessResponse(-1,  "저장에 실패했습니다 : ");
        }
    }

    /**
     * menuRoleId 를 통해 업데이트 진행
     *
     * @param MenuRoleVo menuRoleId 로 검색
     *
     * */
    @Override
    @Transactional
    public ResponseEntity<?> updateMenuRole(MenuRoleVo menuRoleVo) {
        try {
            MenuRoleSearch menuRoleSearch = new MenuRoleSearch();
            menuRoleSearch.setMenuRoleId(menuRoleVo.getMenuRoleId());
            settingMenuRoleBuilder(menuRoleSearch);

            MenuRole selectMenuRole  = selectMenuRoleQuery(menuRoleSearch).fetchOne();

            if(selectMenuRole == null) return ResponseUtil.createResponse(null, -1, "업데이트할 대상이 없습니다.", HttpStatus.OK);


            selectMenuRole.createMenuRole(menuRoleVo);

            return ResponseUtil.createSuccessResponse(1,  "업데이트에 성공했습니다.");
        }catch (Exception e) {
            return ResponseUtil.createResponse(null, -1, "에러가 발생했습니다. 에러 : " + e.getMessage(), HttpStatus.OK);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateMenuRoles(List<MenuRoleVo> menuRoleVos) {
        try {
            for (MenuRoleVo menuRoleVo : menuRoleVos) {
                MenuRoleSearch menuRoleSearch = new MenuRoleSearch();
                menuRoleSearch.setMenuRoleId(menuRoleVo.getMenuRoleId());
                settingMenuRoleBuilder(menuRoleSearch);

                MenuRole selectMenuRole = selectMenuRoleQuery(menuRoleSearch).fetchOne();

                /**
                 * 일단 메뉴 일괄 업데이트가 진행되고 값이 없으면 아무것도 진행안하도록 함.
                 * Todo 어떤 것이 업데이트 되었고 어떤것이 업데이트 되지 못했는지 반환 객체를 만들어서 반환하도록 해야함.
                 * */
                if (selectMenuRole != null) selectMenuRole.createMenuRole(menuRoleVo);
            }

            return ResponseUtil.createSuccessResponse(1, "업데이트에 성공했습니다.");

        } catch (Exception e) {
            return ResponseUtil.createResponse(null, -1, "에러가 발생했습니다. 에러 : " + e.getMessage(), HttpStatus.OK);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> softDeleteMenuRole(Long menuRoleId) {
        try {
            MenuRoleSearch menuRoleSearch = new MenuRoleSearch();
            menuRoleSearch.setMenuRoleId(menuRoleId);
            settingMenuRoleBuilder(menuRoleSearch);

            MenuRole selectMenuRole = selectMenuRoleQuery(menuRoleSearch).fetchOne();


            if(selectMenuRole == null) return ResponseUtil.createSuccessResponse(-1, "삭제할 값이 없습니다.");

            selectMenuRole.softDeleteData(AuthUtil.getLoginUserData().getUserId());

            return ResponseUtil.createSuccessResponse(1, "삭제에 성공했습니다 ");
        }catch (Exception e){
            return ResponseUtil.createSuccessResponse(-1, "에러가 발생했습니다 : " + e.getMessage());
        }

    }

    @Override
    public MenuRoleVo selectMenuRole(MenuRoleSearch menuRoleSearch) {
        try {
            settingMenuRoleBuilder(menuRoleSearch);

            return new MenuRoleVo().convertToVo(
                    Objects.requireNonNull(selectMenuRoleQuery(menuRoleSearch)
                            .fetchOne())
            );
        }catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    @Override
    public List<MenuRoleVo> selectMenuRoleVoList(MenuRoleSearch menuRoleSearch) {

        return new MenuRoleVo()
                .convertToVoList(
                        selectMenuRoleQuery(menuRoleSearch).fetch()
                );
    }

    @Override
    public List<MenuRole> selectMenuRoleList(MenuRoleSearch menuRoleSearch) {

        return selectMenuRoleQuery(menuRoleSearch).fetch();
    }

    @Override
    public JPAQuery<MenuRole> selectMenuRoleQuery(MenuRoleSearch menuRoleSearch) {
        QMenuRole menuRole = QMenuRole.menuRole;

        return queryFactory
                .select(menuRole)
                .from(menuRole)
                .where(settingMenuRoleBuilder(menuRoleSearch));
    }

    @Override
    public BooleanBuilder settingMenuRoleBuilder(MenuRoleSearch menuRoleSearch) {
        BooleanBuilder builder = new BooleanBuilder();

        if(!NullChecker.isNull(menuRoleSearch.getMenuRoleId()))
            builder.and(QMenuRole.menuRole.menuRoleId.eq(menuRoleSearch.getMenuRoleId()));

        if(!NullChecker.isNull(menuRoleSearch.getMenuId()))
            builder.and(QMenuRole.menuRole.menuId.eq(menuRoleSearch.getMenuId()));

        if(!NullChecker.isNull(menuRoleSearch.getRoleId()))
            builder.and(QMenuRole.menuRole.roleId.eq(menuRoleSearch.getRoleId()));

        if(!NullChecker.isNull(menuRoleSearch.getRead()))
            builder.and(QMenuRole.menuRole.read.eq(menuRoleSearch.getRead()));

        if(!NullChecker.isNull(menuRoleSearch.getWrite()))
            builder.and(QMenuRole.menuRole.write.eq(menuRoleSearch.getWrite()));

        if(!NullChecker.isNull(menuRoleSearch.getDownload()))
            builder.and(QMenuRole.menuRole.download.eq(menuRoleSearch.getDownload()));

        if(!NullChecker.isNull(menuRoleSearch.getUpload()))
            builder.and(QMenuRole.menuRole.upload.eq(menuRoleSearch.getUpload()));

        builder.and(QMenuRole.menuRole.isDeleted.eq(false));

        return builder;
    }

}
