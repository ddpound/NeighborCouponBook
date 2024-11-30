package com.neighborcouponbook.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMenuRole is a Querydsl query type for MenuRole
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMenuRole extends EntityPathBase<MenuRole> {

    private static final long serialVersionUID = 2119293850L;

    public static final QMenuRole menuRole = new QMenuRole("menuRole");

    public final QCommonColumn _super = new QCommonColumn(this);

    //inherited
    public final DateTimePath<java.sql.Timestamp> createDate = _super.createDate;

    //inherited
    public final NumberPath<Long> createUser = _super.createUser;

    //inherited
    public final StringPath dbRemarks = _super.dbRemarks;

    public final BooleanPath download = createBoolean("download");

    //inherited
    public final BooleanPath isDeleted = _super.isDeleted;

    public final NumberPath<Long> menuId = createNumber("menuId", Long.class);

    public final NumberPath<Long> menuRoleId = createNumber("menuRoleId", Long.class);

    public final BooleanPath read = createBoolean("read");

    //inherited
    public final StringPath remarks = _super.remarks;

    public final NumberPath<Long> roleId = createNumber("roleId", Long.class);

    //inherited
    public final DateTimePath<java.sql.Timestamp> updateDate = _super.updateDate;

    //inherited
    public final NumberPath<Long> updateUser = _super.updateUser;

    public final BooleanPath upload = createBoolean("upload");

    public final BooleanPath write = createBoolean("write");

    public QMenuRole(String variable) {
        super(MenuRole.class, forVariable(variable));
    }

    public QMenuRole(Path<? extends MenuRole> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMenuRole(PathMetadata metadata) {
        super(MenuRole.class, metadata);
    }

}

