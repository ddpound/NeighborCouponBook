package com.neighborcouponbook.config.init;

import com.neighborcouponbook.common.annotation.MenuInformation;
import com.neighborcouponbook.common.response.ApiCommonResponse;
import com.neighborcouponbook.model.CouponUser;
import com.neighborcouponbook.model.Menu;
import com.neighborcouponbook.model.Role;
import com.neighborcouponbook.model.UserRole;
import com.neighborcouponbook.model.search.CouponUserSearch;
import com.neighborcouponbook.model.search.RoleSearch;
import com.neighborcouponbook.model.search.UserRoleSearch;
import com.neighborcouponbook.model.vo.*;
import com.neighborcouponbook.repository.UserRoleRepository;
import com.neighborcouponbook.service.*;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 앱 초기 실행시 생성되는 데이터
 *
 * */
@Log4j2
@RequiredArgsConstructor
@Component
public class InitSystem implements CommandLineRunner {

    private final UserRoleRepository userRoleRepository;

    @Value("${admin-setting.super-admin-id}")
    private Long superAdminId;

    @Value("${admin-setting.super-admin-login-id}")
    private String superAdminLoginId;

    @Value("${admin-setting.super-admin-pw}")
    private String superAdminPassword;

    @Value("${admin-setting.super-admin-role-name}")
    private String superAdminRoleName;

    @Value("${admin-setting.super-admin-role-id}")
    private Long superAdminRoleId;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlAuto;

    private final RequestMappingHandlerMapping handlerMapping;

    private final CouponUserService couponUserService;

    private final RoleService roleService;

    private final UserRoleService userRoleService;

    private final MenuService menuService;

    private final MenuAuthorizationService menuAuthorizationService;

    private final MenuRoleService menuRoleService;

    @Override
    public void run(String... args) throws Exception {

        // DB 세팅, ddlAuto가 create 상태일때만 초기 세팅이 작동한다.
        if(ddlAuto.equals("create")){
            createSuperAdmin();
            createSuperAdminRole();
            createSuperAdminUserRole();

            createUserRole();

            // 엔드포인트에 등록된 메뉴 추가
            createBaseMenuList();
        }


    }

    public void createSuperAdmin() {

        CouponUserVo selectCouponUserVo = couponUserService
                .selectCouponUser(
                        CouponUserSearch.builder()
                                .userId(superAdminId).build());


        if(selectCouponUserVo == null) {

            // super user 생성
            CouponUser superAdminUser = new CouponUser();
            superAdminUser.settingCouponUser(superAdminLoginId, superAdminLoginId, superAdminPassword);
            superAdminUser.settingCreateData(superAdminId);
            superAdminUser.writeDbRemarks(superAdminRoleName);

            couponUserService.createCouponUser(superAdminUser);
        }

    }

    public void createSuperAdminRole(){

        List<RoleVo> selectList = roleService
                .selectRoleList(RoleSearch.builder().roleId(superAdminRoleId).build());

        if(selectList == null) {
            // super role 생성
            Role superRole = new Role();
            superRole.settingCreateData(superAdminId);
            superRole.createRole(superAdminRoleName);
            superRole.writeDbRemarks("Super Role");


            roleService.saveRole(superRole);
        }
    }

    public void createUserRole(){

        List<RoleVo> selectList = roleService
                .selectRoleList(RoleSearch.builder().roleId(2L).build());

        if(selectList == null) {
            // user role 생성
            Role superRole = new Role();
            superRole.settingCreateData(superAdminId);
            superRole.createRole("user");
            superRole.writeDbRemarks("Normal User Role");

            roleService.saveRole(superRole);
        }
    }

    public void createSuperAdminUserRole(){

        List<UserRoleVo> selectList = userRoleService
                .selectUserRoleList(UserRoleSearch
                        .builder()
                        .userRoleId(superAdminId)
                        .build());

        if(selectList == null){
            UserRole superAdminUserRole = new UserRole();

            superAdminUserRole.createUserRole(superAdminId,superAdminRoleId);
            superAdminUserRole.settingCreateData(superAdminId);
            superAdminUserRole.writeDbRemarks("Super Admin UserRole");

            userRoleService.saveUserRole(superAdminUserRole);
        }
    }

    public void createBaseMenuList(){
        // 모든 엔드포인트 매핑 정보 가져오기
        Map<RequestMappingInfo, HandlerMethod> mappings = handlerMapping.getHandlerMethods();
        List<MenuVo> allMenu = menuService.selectAllMenus();

        mappings.forEach((mappingInfo, handlerMethod) -> {
            // Swagger Operation 어노테이션 정보 가져오기
            Operation operation = handlerMethod.getMethodAnnotation(Operation.class);
            MenuInformation menuInformation = handlerMethod.getMethod().getAnnotation(MenuInformation.class);

            if(menuInformation != null && menuInformation.menuDirectDBSave()){
                String description;
                String name;

                if (operation != null) {
                    description = operation.description();
                    name = operation.summary();
                } else {
                    name = "";
                    description = "";
                }

                // URL 패턴 가져오기
                Set<String> patterns = mappingInfo.getPatternValues();

                patterns.forEach(pattern -> {
                    MenuVo menu = MenuVo.builder()
                            .menuUri(pattern)
                            .menuName(name.isEmpty() ? pattern : name) // 적절한 메뉴 이름 설정 필요
                            .menuProperty(pattern.contains("{") ? Menu.MenuProperty.DYNAMIC : Menu.MenuProperty.STATIC)
                            .menuType(menuInformation != null ? menuInformation.menuType() : null)
                            .dbRemarks(description.isEmpty() ? "초기 세팅 메뉴 리스트" : description)
                            .build();

                    ApiCommonResponse<?> returnResponse = null;

                    // list static, dynamic 구분 리스트
                    if(allMenu != null && !allMenu.isEmpty()) {
                        List<MenuVo> menus = menuAuthorizationService.searchFilteringMenuList(allMenu, pattern);

                        // menu가 존재 하지 않을때만 저장
                        if (menus == null) {
                            returnResponse = (ApiCommonResponse<?>) menuService.createMenu(menu).getBody();
                            allMenu.add(menu);
                        }
                    }else{
                        returnResponse = (ApiCommonResponse<?>) menuService.createMenu(menu).getBody();
                        allMenu.add(menu);
                    }

                    Menu newMenu = returnResponse != null && returnResponse.getData() != null ? (Menu) returnResponse.getData() : null;

                    // 메뉴 추가가 완료되었으면 메뉴에 해당된 권한도 추가.
                    // 메뉴가 올바르게 저장되어야함.
                    if(menuInformation != null && menuInformation.menuAuthDetail() != null && menuInformation.menuAuthDetail().length > 0 && newMenu != null) {
                        // 메뉴 등록 추가
                        for (MenuInformation.MenuRoleDetail detail : menuInformation.menuAuthDetail()) {
                            if(detail.menuRoleDirectDBSave()){
                                menuRoleService.saveMenuRole(
                                        MenuRoleVo.builder()
                                                .menuId(newMenu.getMenuId())
                                                .roleId(detail.roleId())
                                                .read(detail.read())
                                                .write(detail.write())
                                                .upload(detail.upload())
                                                .download(detail.download())
                                                .dbRemarks("초기 구동시 자동 입력된 데이터")
                                                .isDeleted(false)
                                                .build());
                            }
                        }
                    }
                });
            }
        });
    }
}
