package com.neighborcouponbook.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCoupon is a Querydsl query type for Coupon
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCoupon extends EntityPathBase<Coupon> {

    private static final long serialVersionUID = 713062027L;

    public static final QCoupon coupon = new QCoupon("coupon");

    public final QCommonColumn _super = new QCommonColumn(this);

    public final StringPath couponDescription = createString("couponDescription");

    public final NumberPath<Long> couponId = createNumber("couponId", Long.class);

    //inherited
    public final DateTimePath<java.sql.Timestamp> createDate = _super.createDate;

    //inherited
    public final NumberPath<Long> createUser = _super.createUser;

    //inherited
    public final StringPath dbRemarks = _super.dbRemarks;

    public final NumberPath<Integer> goalsNumber = createNumber("goalsNumber", Integer.class);

    //inherited
    public final BooleanPath isDeleted = _super.isDeleted;

    //inherited
    public final StringPath remarks = _super.remarks;

    public final NumberPath<Long> shopId = createNumber("shopId", Long.class);

    //inherited
    public final DateTimePath<java.sql.Timestamp> updateDate = _super.updateDate;

    //inherited
    public final NumberPath<Long> updateUser = _super.updateUser;

    public QCoupon(String variable) {
        super(Coupon.class, forVariable(variable));
    }

    public QCoupon(Path<? extends Coupon> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCoupon(PathMetadata metadata) {
        super(Coupon.class, metadata);
    }

}

