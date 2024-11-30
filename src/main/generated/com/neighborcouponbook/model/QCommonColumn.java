package com.neighborcouponbook.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCommonColumn is a Querydsl query type for CommonColumn
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QCommonColumn extends EntityPathBase<CommonColumn> {

    private static final long serialVersionUID = 1012730822L;

    public static final QCommonColumn commonColumn = new QCommonColumn("commonColumn");

    public final DateTimePath<java.sql.Timestamp> createDate = createDateTime("createDate", java.sql.Timestamp.class);

    public final NumberPath<Long> createUser = createNumber("createUser", Long.class);

    public final StringPath dbRemarks = createString("dbRemarks");

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final StringPath remarks = createString("remarks");

    public final DateTimePath<java.sql.Timestamp> updateDate = createDateTime("updateDate", java.sql.Timestamp.class);

    public final NumberPath<Long> updateUser = createNumber("updateUser", Long.class);

    public QCommonColumn(String variable) {
        super(CommonColumn.class, forVariable(variable));
    }

    public QCommonColumn(Path<? extends CommonColumn> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCommonColumn(PathMetadata metadata) {
        super(CommonColumn.class, metadata);
    }

}

