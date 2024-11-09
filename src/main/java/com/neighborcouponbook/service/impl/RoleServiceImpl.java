package com.neighborcouponbook.service.impl;

import com.neighborcouponbook.common.util.NullChecker;
import com.neighborcouponbook.model.QRole;
import com.neighborcouponbook.model.Role;
import com.neighborcouponbook.model.search.RoleSearch;
import com.neighborcouponbook.model.vo.RoleVo;
import com.neighborcouponbook.repository.RoleRepository;
import com.neighborcouponbook.service.RoleService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Log4j2
@Service
public class RoleServiceImpl implements RoleService {

    private final JPAQueryFactory queryFactory;

    private final RoleRepository roleRepository;

    @Transactional(readOnly = true)
    @Override
    public List<RoleVo> selectRoleList(RoleSearch search) {

        QRole qRole = QRole.role;
        BooleanBuilder builder = new BooleanBuilder();

        if(!NullChecker.isNullString(search.getRoleId())) builder.and(qRole.roleId.eq(search.getRoleId()));
        if(!NullChecker.isNullString(search.getRoleName())) builder.and(qRole.roleName.eq(search.getRoleName()));

        List<Role> roleList = queryFactory.selectFrom(qRole).where(builder).fetch();

        if(roleList != null && !roleList.isEmpty()) return new RoleVo().convertToRoleVoList(roleList);
        else return null;
    }

    @Transactional
    @Override
    public int saveRole(Role role) {
        roleRepository.save(role);
        return 1;
    }

    @Transactional
    @Override
    public int updateRole(Role role) {
        return 1;
    }

    @Transactional
    @Override
    public int softDeleteRole(Long id) {

        Optional<Role> dbRole = roleRepository.findById(id);

        dbRole.ifPresent(role -> {
            role.softDeleteData(1L);
        });

        return 1;
    }
}
