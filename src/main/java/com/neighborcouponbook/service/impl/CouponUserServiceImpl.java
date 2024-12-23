package com.neighborcouponbook.service.impl;

import com.neighborcouponbook.common.response.ApiCommonResponse;
import com.neighborcouponbook.common.response.ResponseMetaData;
import com.neighborcouponbook.common.response.ResponseUtil;
import com.neighborcouponbook.common.util.AuthUtil;
import com.neighborcouponbook.common.util.NullChecker;
import com.neighborcouponbook.model.CouponUser;
import com.neighborcouponbook.model.QCouponUser;
import com.neighborcouponbook.model.QUserRole;
import com.neighborcouponbook.model.UserRole;
import com.neighborcouponbook.model.search.CommonSearch;
import com.neighborcouponbook.model.search.CouponUserSearch;
import com.neighborcouponbook.model.vo.CouponUserVo;
import com.neighborcouponbook.model.vo.CouponUserWithUserRole;
import com.neighborcouponbook.model.vo.PasswordChangeRequest;
import com.neighborcouponbook.model.vo.UserRoleVo;
import com.neighborcouponbook.repository.CouponUserRepository;
import com.neighborcouponbook.service.CouponUserService;
import com.neighborcouponbook.service.UserRoleService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Log4j2
@Service
public class CouponUserServiceImpl implements CouponUserService {

    private final JPAQueryFactory queryFactory;

    private final CouponUserRepository couponUserRepository;

    private final UserRoleService userRoleService;

    private final PasswordEncoder passwordEncoder;

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
    public ResponseEntity<ApiCommonResponse<String>> createCouponUser(CouponUserVo couponUserVo) {
        try {
            ResponseEntity<ApiCommonResponse<String>> validationData = couponUserVoValidation(couponUserVo);
            if(validationData != null) return validationData;

            // 검증 완료 후 유저 데이터 저장
            CouponUser complteUser = couponUserRepository
                    .save(settingCreateUserWithCreateData(couponUserVo, AuthUtil.getLoginUserId() != null ? AuthUtil.getLoginUserId() : 1L));

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
            if(validationData != null) {
                ApiCommonResponse<?> apiCommonResponse = (ApiCommonResponse<?>) validationData.getBody();
                log.info("USER JOIN -> validation check message : {}", apiCommonResponse.getMessage());
                return validationData;
            }

            couponUserVo.setDbRemarks("회원가입으로 생성된 유저");

            // 초기 회원가입 데이터라 security context에 담긴 값이 없음.
            CouponUser complteUser = couponUserRepository
                    .save(settingCreateUserWithCreateData(couponUserVo,1L));

            // 먼저 권한을 가져온다. 초기 유저 생성시 권한은 일반 유저다. 2가 일반유저 아이디
            UserRole createUserRole = new UserRole();
            createUserRole.createUserRole(complteUser.getUserId(), 2L);
            createUserRole.settingCreateData(complteUser.getUserId());
            createUserRole.writeRemarks("회원가입으로 생성된 유저 권한 데이터");
            userRoleService.saveUserRole(createUserRole);

            return ResponseUtil.createSuccessResponse(1, "저장에 성공했습니다.");
        }catch (Exception e){
            return ResponseUtil.createSuccessResponse(-1, "저장에 실패했습니다 : " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<ApiCommonResponse<String>> couponUserVoValidation(CouponUserVo couponUserVo) {
        if(couponUserVo.getUserLoginId() == null) return ResponseUtil.createSuccessResponse(-1, "입력 로그인 아이디가 없습니다.");
        if(couponUserVo.getUserName() == null || couponUserVo.getUserName().isEmpty()) return ResponseUtil.createSuccessResponse(-1, "유저 이름이 없습니다.");
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

    @Override
    public List<CouponUserWithUserRole> selectCouponUserQueryJoinUserRole(CouponUserSearch search) {
        try {
            List<CouponUserWithUserRole> resultList = new ArrayList<>();

            QCouponUser qCouponUser = QCouponUser.couponUser;
            QUserRole qUserRole = QUserRole.userRole;

            JPAQuery<Tuple> result = queryFactory
                    .select(qCouponUser,qUserRole)
                    .from(qCouponUser)
                    .join(qUserRole).on(qCouponUser.userId.eq(qUserRole.userId))
                    .where(settingCouponUserSearchBuilder(search))
                    .offset(search.getOffset())
                    .limit(search.getPageSize());

            result = applySorting(result, search, qCouponUser);

            for (Tuple tuple : result.fetch()) {
                CouponUser couponUser = tuple.get(qCouponUser);
                UserRole userRoleResult = tuple.get(qUserRole);

                CouponUserWithUserRole couponUserWithUserRole = new CouponUserWithUserRole();
                couponUserWithUserRole.setCouponUserVo(new CouponUserVo().convertToVo(couponUser));
                couponUserWithUserRole.setUserRoleVo(new UserRoleVo().convertToUserRoleVo(userRoleResult));

                resultList.add(couponUserWithUserRole);
            }

            return resultList;
        }catch (Exception e){
            log.error(e.getMessage());
            return null;
        }
    }

    private JPAQuery<Tuple> applySorting(JPAQuery<Tuple> query, CouponUserSearch couponUserSearch, QCouponUser qCouponUser) {
        if(couponUserSearch.getSort() == null || couponUserSearch.getSortOrder() == null) return query;

        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(couponUserSearch, qCouponUser);
        if (orderSpecifier != null) {
            query = query.orderBy(orderSpecifier);
        }

        return query;
    }

    private OrderSpecifier<?> getOrderSpecifier(CouponUserSearch couponUserSearch, QCouponUser qCouponUser) {
        return switch (couponUserSearch.getSort()){
            case userId -> couponUserSearch.getSortOrder().equals(CommonSearch.OrderBy.asc) ? qCouponUser.userId.asc() : qCouponUser.userId.desc();
            case userLoginId -> couponUserSearch.getSortOrder().equals(CommonSearch.OrderBy.asc) ? qCouponUser.userLoginId.asc() : qCouponUser.userLoginId.desc();
            case userName -> couponUserSearch.getSortOrder().equals(CommonSearch.OrderBy.asc) ? qCouponUser.userName.asc() : qCouponUser.userName.desc();
        };
    }

    @Override
    public Long selectCouponUserQueryJoinUserRoleTotalCount(CouponUserSearch search) {

        QCouponUser qCouponUser = QCouponUser.couponUser;
        QUserRole qUserRole = QUserRole.userRole;

        return queryFactory
                .select(qCouponUser.count())
                .from(qCouponUser)
                .join(qUserRole).on(qCouponUser.userId.eq(qUserRole.userId))
                .where(settingCouponUserSearchBuilder(search))
                .fetchOne();
    }

    @Override
    public ResponseEntity<?> responseSelectCouponUserQueryJoinUserRole(CouponUserSearch search) {
        try {
            List<CouponUserWithUserRole> resultList = selectCouponUserQueryJoinUserRole(search);
            Long totalCount = selectCouponUserQueryJoinUserRoleTotalCount(search);

            return ResponseUtil.createResponse(
                    resultList,
                    ResponseMetaData.builder().dataTotalCount(totalCount).build(),
                    1,
                    "어드민 유저 리스트 반환 완료",
                    HttpStatus.OK
            );
        } catch (Exception e) {
            log.error(e.getMessage());

            return ResponseUtil.createResponse(
                    null,
                    ResponseMetaData.builder().dataDescription(e.getMessage()).build(),
                    -1,
                    "에러 발생",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    /**
     * userdata 전체 수정
     * user data는 운영자가 아니면 자기 자신만 수정 가능합니다.
     * */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseEntity<ApiCommonResponse<CouponUserVo>> updateCouponUser(CouponUserVo couponUserVo) {
        try {
            Optional<CouponUser> findUser = couponUserRepository.findById(couponUserVo.getUserId());
            CouponUserVo returnCouponUserVo = new CouponUserVo();

            if(findUser.isEmpty()) return ResponseUtil.createErrorResponse("데이터가 없습니다.");
            if(Objects.equals(AuthUtil.getLoginUserId(), findUser.get().getUserId())) return ResponseUtil.createErrorResponse("자기 자신만 자신의 데이터를 수정 할수 있습니다.");

            // 더티체킹
            findUser.get().updateCouponUser(
                    couponUserVo.getUserLoginId(),
                    couponUserVo.getUserName()
            );

            // user type 변경
            if(couponUserVo.getCouponUserType() != null) {
                findUser.get().initUserType(couponUserVo.getCouponUserType());
            }

            return ResponseUtil.createSuccessResponse(
                    returnCouponUserVo.convertToVo(findUser.get()),
                    "업데이트가 완료되었습니다."
            );
        } catch (NullPointerException e) {
            return ResponseUtil.createErrorResponse("데이터에 빈 값이 있습니다.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseEntity<ApiCommonResponse<CouponUserVo>> updateCouponUserType(CouponUserVo couponUserVo) {
        try {
            Optional<CouponUser> findUser = couponUserRepository.findById(couponUserVo.getUserId());
            CouponUserVo returnCouponUserVo = new CouponUserVo();

            if(findUser.isEmpty()) return ResponseUtil.createErrorResponse("데이터가 없습니다.");
            if(Objects.equals(AuthUtil.getLoginUserId(), findUser.get().getUserId())) return ResponseUtil.createErrorResponse("자기 자신만 자신의 데이터를 수정 할수 있습니다.");

            // user type 변경
            if(couponUserVo.getCouponUserType() != null) {
                findUser.get().initUserType(couponUserVo.getCouponUserType());
            }

            return ResponseUtil.createSuccessResponse(
                    returnCouponUserVo.convertToVo(findUser.get()),
                    "업데이트가 완료되었습니다."
            );
        } catch (NullPointerException e) {
            return ResponseUtil.createErrorResponse("데이터에 빈 값이 있습니다.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseEntity<ApiCommonResponse<CouponUserVo>> couponUserPasswordChange(PasswordChangeRequest passwordChangeRequest) {

        try {
            if(new PasswordChangeRequest().validate(passwordChangeRequest))
                return ResponseUtil.createErrorResponse("필수 입력값이 빠졌습니다.", HttpStatus.BAD_REQUEST);

            Optional<CouponUser> findUser = couponUserRepository.findById(passwordChangeRequest.getUserId());

            if(findUser.isEmpty()) return ResponseUtil.createErrorResponse("데이터가 없습니다.");
            if(Objects.equals(AuthUtil.getLoginUserId(), findUser.get().getUserId())) return ResponseUtil.createErrorResponse("자기 자신만 자신의 데이터를 수정 할수 있습니다.");
            if(!passwordEncoder.matches(passwordChangeRequest.getOldPassword(), findUser.get().getPassword())) return ResponseUtil.createErrorResponse("비밀번호가 다릅니다.");

            findUser.get().changePassword(passwordChangeRequest.getNewPassword());

            return ResponseUtil.createSuccessResponse(new CouponUserVo().convertToVo(findUser.get()), "비밀번호 변경에 성공했습니다.");
        } catch (NullPointerException e) {
            return ResponseUtil.createErrorResponse("데이터에 빈 값이 있습니다.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
