package com.neighborcouponbook.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QShop is a Querydsl query type for Shop
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QShop extends EntityPathBase<Shop> {

    private static final long serialVersionUID = 1721880219L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QShop shop = new QShop("shop");

    public final QCommonColumn _super = new QCommonColumn(this);

    public final StringPath businessRegistrationNumber = createString("businessRegistrationNumber");

    public final QCouponUser couponUser;

    //inherited
    public final DateTimePath<java.sql.Timestamp> createDate = _super.createDate;

    //inherited
    public final NumberPath<Long> createUser = _super.createUser;

    //inherited
    public final StringPath dbRemarks = _super.dbRemarks;

    //inherited
    public final BooleanPath isDeleted = _super.isDeleted;

    //inherited
    public final StringPath remarks = _super.remarks;

    public final StringPath shopAddress = createString("shopAddress");

    public final StringPath shopDescription = createString("shopDescription");

    public final NumberPath<Long> shopId = createNumber("shopId", Long.class);

    public final StringPath shopName = createString("shopName");

    public final QShopType shopType;

    //inherited
    public final DateTimePath<java.sql.Timestamp> updateDate = _super.updateDate;

    //inherited
    public final NumberPath<Long> updateUser = _super.updateUser;

    public QShop(String variable) {
        this(Shop.class, forVariable(variable), INITS);
    }

    public QShop(Path<? extends Shop> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QShop(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QShop(PathMetadata metadata, PathInits inits) {
        this(Shop.class, metadata, inits);
    }

    public QShop(Class<? extends Shop> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.couponUser = inits.isInitialized("couponUser") ? new QCouponUser(forProperty("couponUser")) : null;
        this.shopType = inits.isInitialized("shopType") ? new QShopType(forProperty("shopType")) : null;
    }

}

