package com.neighborcouponbook.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCouponBookFile is a Querydsl query type for CouponBookFile
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCouponBookFile extends EntityPathBase<CouponBookFile> {

    private static final long serialVersionUID = 841164720L;

    public static final QCouponBookFile couponBookFile = new QCouponBookFile("couponBookFile");

    public final QCommonColumn _super = new QCommonColumn(this);

    //inherited
    public final DateTimePath<java.sql.Timestamp> createDate = _super.createDate;

    //inherited
    public final NumberPath<Long> createUser = _super.createUser;

    //inherited
    public final StringPath dbRemarks = _super.dbRemarks;

    public final NumberPath<Long> fileGroupId = createNumber("fileGroupId", Long.class);

    public final NumberPath<Long> fileId = createNumber("fileId", Long.class);

    public final NumberPath<Long> fileSerialNo = createNumber("fileSerialNo", Long.class);

    //inherited
    public final BooleanPath isDeleted = _super.isDeleted;

    public final StringPath originalFileName = createString("originalFileName");

    public final StringPath physicalFileName = createString("physicalFileName");

    public final StringPath physicalFilePath = createString("physicalFilePath");

    //inherited
    public final StringPath remarks = _super.remarks;

    //inherited
    public final DateTimePath<java.sql.Timestamp> updateDate = _super.updateDate;

    //inherited
    public final NumberPath<Long> updateUser = _super.updateUser;

    public QCouponBookFile(String variable) {
        super(CouponBookFile.class, forVariable(variable));
    }

    public QCouponBookFile(Path<? extends CouponBookFile> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCouponBookFile(PathMetadata metadata) {
        super(CouponBookFile.class, metadata);
    }

}

