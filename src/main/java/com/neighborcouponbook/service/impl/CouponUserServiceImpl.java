package com.neighborcouponbook.service.impl;

import com.neighborcouponbook.common.response.ResponseUtil;
import com.neighborcouponbook.common.util.AuthUtil;
import com.neighborcouponbook.common.util.NullChecker;
import com.neighborcouponbook.model.CouponUser;
import com.neighborcouponbook.model.QCouponUser;
import com.neighborcouponbook.model.UserRole;
import com.neighborcouponbook.model.search.CouponUserSearch;
import com.neighborcouponbook.model.vo.CouponUserVo;
import com.neighborcouponbook.repository.CouponUserRepository;
import com.neighborcouponbook.service.CouponUserService;
import com.neighborcouponbook.service.UserRoleService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Log4j2
@Service
public class CouponUserServiceImpl implements CouponUserService {

    private final JPAQueryFactory queryFactory;

    private final CouponUserRepository couponUserRepository;

    private final UserRoleService userRoleService;

    @Transactional
    @Override
    public Integer createCouponUser(CouponUser couponUser) {
        try {
            couponUserRepository.save(couponUser);
            return 1;
        }catch (Exception e) {
            return -1;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseEntity<?> createCouponUser(CouponUserVo couponUserVo) {
        try {
            ResponseEntity<?> validationData = couponUserVoValidation(couponUserVo);
            if(validationData != null) return validationData;

            // 검증 완료 후 유저 데이터 저장
            CouponUser complteUser = couponUserRepository
                    .save(settingCreateUserWithCreateData(couponUserVo, AuthUtil.getLoginUserData().getUserId()));

            // 먼저 권한을 가져온다. 초기 유저 생성시 권한은 일반 유저다.  2가 일반유저 아이디
            UserRole createUserRole = new UserRole();
            createUserRole.createUserRole(complteUser.getUserId(), 2L);
            userRoleService.saveUserRole(createUserRole);
            return ResponseUtil.createSuccessResponse(1, "저장에 성공했습니다.");
        }catch (Exception e) {
            return ResponseUtil.createSuccessResponse(-1, "저장에 실패했습니다 : " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> joinCouponUser(CouponUserVo couponUserVo) {
        try {
            ResponseEntity<?> validationData = couponUserVoValidation(couponUserVo);
            if(validationData != null) return validationData;

            // 초기 회원가입 데이터라 security context에 담긴 값이 없음.
            CouponUser complteUser = couponUserRepository
                    .save(settingCreateUserWithCreateData(couponUserVo,1L));

            // 먼저 권한을 가져온다. 초기 유저 생성시 권한은 일반 유저다. 2가 일반유저 아이디
            UserRole createUserRole = new UserRole();
            createUserRole.createUserRole(complteUser.getUserId(), 2L);
            userRoleService.saveUserRole(createUserRole);

            return ResponseUtil.createSuccessResponse(1, "저장에 성공했습니다.");
        }catch (Exception e){
            return ResponseUtil.createSuccessResponse(-1, "저장에 실패했습니다 : " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> couponUserVoValidation(CouponUserVo couponUserVo) {
        if(couponUserVo.getUserLoginId() == null) return ResponseUtil.createSuccessResponse(-1, "입력 로그인 아이디가 없습니다.");
        if(couponUserVo.getUserName() == null) return ResponseUtil.createSuccessResponse(-1, "유저 이름이 없습니다.");
        if(loginIdDuplicateCheck(couponUserVo)) return ResponseUtil.createSuccessResponse(-1, "이미 있는 로그인 아이디 입니다.");

        return null;
    }

    @Override
    public Boolean loginIdDuplicateCheck(CouponUserVo couponUserVo) {
        List<CouponUserVo> selectVoList = selectCouponUserList(CouponUserSearch.builder().userLoginId(couponUserVo.getUserLoginId()).build());
        return selectVoList != null && !selectVoList.isEmpty();
    }

    @Override
    public List<CouponUserVo> selectCouponUserList(CouponUserSearch search) {

        settingCouponUserSearchBuilder(search);
        List<CouponUser> resultList = selectCouponUserQuery(search).fetch();

        log.info("result search list count: {}", resultList != null ? resultList.size() : 0);

        CouponUserVo vo = new CouponUserVo();
        return vo.convertDtoToList(Objects.requireNonNull(resultList));
    }

    @Override
    public CouponUserVo selectCouponUser(CouponUserSearch search) {

        CouponUser resultList = selectCouponUserQuery(search).fetchOne();

        CouponUserVo vo = new CouponUserVo();
        if(resultList != null) return vo.convertToVo(resultList);
        else return null;
    }

    @Override
    public CouponUser settingCreateUserWithCreateData(CouponUserVo couponUserVo, Long insertUserId) {
        // 검증 완료 유저 생성 시작
        CouponUser createUser = new CouponUser();
        createUser.settingCouponUser(couponUserVo.getUserLoginId(), couponUserVo.getUserName(), couponUserVo.getPassword());

        createUser.settingCreateData(insertUserId);

        // 주석, DB 전용 주석을 따로 지정해주었다면 주석 작성
        if(couponUserVo.getRemarks() != null) createUser.writeRemarks(couponUserVo.getRemarks());
        if(couponUserVo.getDbRemarks() != null) createUser.writeDbRemarks(couponUserVo.getDbRemarks());

        return createUser;
    }

    @Override
    public String selectUserPassword(CouponUserSearch search) {
        CouponUser selectCouponUser = selectCouponUserQuery(search).fetchOne();

        if(selectCouponUser == null) return null;
        return selectCouponUser.getPassword();
    }

    @Override
    public BooleanBuilder settingCouponUserSearchBuilder(CouponUserSearch search) {
        BooleanBuilder builder = new BooleanBuilder();

        if(!NullChecker.isNullString(search.getUserId())) builder.and(QCouponUser.couponUser.userId.eq(search.getUserId()));
        if(!NullChecker.isNullString(search.getUserLoginId())) builder.and(QCouponUser.couponUser.userLoginId.eq(search.getUserLoginId()));
        if(!NullChecker.isNullString(search.getUserName())) builder.and(QCouponUser.couponUser.userName.eq(search.getUserName()));

        builder.and(QCouponUser.couponUser.isDeleted.eq(false));

        return builder;
    }

    @Override
    public JPAQuery<CouponUser> selectCouponUserQuery(CouponUserSearch search) {
        QCouponUser qCouponUser = QCouponUser.couponUser;

        return queryFactory
                .select(qCouponUser)
                .from(qCouponUser)
                .where(settingCouponUserSearchBuilder(search));
    }

}
