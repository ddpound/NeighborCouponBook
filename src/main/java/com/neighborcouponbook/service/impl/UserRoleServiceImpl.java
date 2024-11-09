package com.neighborcouponbook.service.impl;

import com.neighborcouponbook.common.util.NullChecker;
import com.neighborcouponbook.model.QUserRole;
import com.neighborcouponbook.model.UserRole;
import com.neighborcouponbook.model.search.UserRoleSearch;
import com.neighborcouponbook.model.vo.UserRoleVo;
import com.neighborcouponbook.repository.UserRoleRepository;
import com.neighborcouponbook.service.UserRoleService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Service
public class UserRoleServiceImpl implements UserRoleService {

    private final JPAQueryFactory queryFactory;

    private final UserRoleRepository userRoleRepository;

    @Override
    public List<UserRoleVo> selectUserRoleList(UserRoleSearch search) {
        QUserRole qUserRole = QUserRole.userRole;
        BooleanBuilder builder = new BooleanBuilder();

        if(!NullChecker.isNull(search.getUserRoleId())) builder.and(qUserRole.userRoleId.eq(search.getUserRoleId()));
        if(!NullChecker.isNull(search.getUserId())) builder.and(qUserRole.userId.eq(search.getUserId()));
        if(!NullChecker.isNull(search.getRoleId())) builder.and(qUserRole.roleId.eq(search.getRoleId()));

        List<UserRole> resultList = queryFactory.selectFrom(qUserRole).where(builder).fetch();

        if(resultList != null && !resultList.isEmpty()) return new UserRoleVo().convertToUserRoleVoList(resultList);
        else return null;
    }

    @Transactional
    @Override
    public UserRole selectUserRole(Long userRoleId) {
        return userRoleRepository.findById(userRoleId).get();
    }

    @Transactional
    @Override
    public Integer saveUserRole(UserRole userRole) {
        try{
            userRoleRepository.save(userRole);
            return 1;
        }catch (Exception e){
            return -1;
        }
    }
}
