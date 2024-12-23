package com.neighborcouponbook.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCouponUser is a Querydsl query type for CouponUser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCouponUser extends EntityPathBase<CouponUser> {

    private static final long serialVersionUID = 1898223862L;

    public static final QCouponUser couponUser = new QCouponUser("couponUser");

    public final QCommonColumn _super = new QCommonColumn(this);

    public final EnumPath<CouponUser.CouponUserType> couponUserType = createEnum("couponUserType", CouponUser.CouponUserType.class);

    //inherited
    public final DateTimePath<java.sql.Timestamp> createDate = _super.createDate;

    //inherited
    public final NumberPath<Long> createUser = _super.createUser;

    //inherited
    public final StringPath dbRemarks = _super.dbRemarks;

    //inherited
    public final BooleanPath isDeleted = _super.isDeleted;

    public final StringPath password = createString("password");

    //inherited
    public final StringPath remarks = _super.remarks;

    //inherited
    public final DateTimePath<java.sql.Timestamp> updateDate = _super.updateDate;

    //inherited
    public final NumberPath<Long> updateUser = _super.updateUser;

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public final StringPath userLoginId = createString("userLoginId");

    public final StringPath userName = createString("userName");

    public QCouponUser(String variable) {
        super(CouponUser.class, forVariable(variable));
    }

    public QCouponUser(Path<? extends CouponUser> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCouponUser(PathMetadata metadata) {
        super(CouponUser.class, metadata);
    }

}

