package com.neighborcouponbook.config.init;

import com.neighborcouponbook.model.CouponUser;
import com.neighborcouponbook.model.Role;
import com.neighborcouponbook.model.UserRole;
import com.neighborcouponbook.model.search.CouponUserSearch;
import com.neighborcouponbook.model.search.RoleSearch;
import com.neighborcouponbook.model.search.UserRoleSearch;
import com.neighborcouponbook.model.vo.CouponUserVo;
import com.neighborcouponbook.model.vo.RoleVo;
import com.neighborcouponbook.model.vo.UserRoleVo;
import com.neighborcouponbook.repository.UserRoleRepository;
import com.neighborcouponbook.service.CouponUserService;
import com.neighborcouponbook.service.RoleService;
import com.neighborcouponbook.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

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

    private final CouponUserService couponUserService;

    private final RoleService roleService;

    private final UserRoleService userRoleService;

    @Override
    public void run(String... args) throws Exception {
        createSuperAdmin();
        createSuperAdminRole();
        createSuperAdminUserRole();

        createUserRole();
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
}
