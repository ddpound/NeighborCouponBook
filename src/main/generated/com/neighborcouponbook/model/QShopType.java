package com.neighborcouponbook.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QShopType is a Querydsl query type for ShopType
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QShopType extends EntityPathBase<ShopType> {

    private static final long serialVersionUID = -1917121419L;

    public static final QShopType shopType = new QShopType("shopType");

    public final QCommonColumn _super = new QCommonColumn(this);

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

    public final NumberPath<Long> shopTypeId = createNumber("shopTypeId", Long.class);

    public final StringPath shopTypeName = createString("shopTypeName");

    //inherited
    public final DateTimePath<java.sql.Timestamp> updateDate = _super.updateDate;

    //inherited
    public final NumberPath<Long> updateUser = _super.updateUser;

    public QShopType(String variable) {
        super(ShopType.class, forVariable(variable));
    }

    public QShopType(Path<? extends ShopType> path) {
        super(path.getType(), path.getMetadata());
    }

    public QShopType(PathMetadata metadata) {
        super(ShopType.class, metadata);
    }

}

