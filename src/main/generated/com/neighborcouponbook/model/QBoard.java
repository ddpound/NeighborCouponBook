package com.neighborcouponbook.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBoard is a Querydsl query type for Board
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoard extends EntityPathBase<Board> {

    private static final long serialVersionUID = 1823174625L;

    public static final QBoard board = new QBoard("board");

    public final QCommonColumn _super = new QCommonColumn(this);

    public final StringPath boardContent = createString("boardContent");

    public final NumberPath<Long> boardId = createNumber("boardId", Long.class);

    public final StringPath boardTitle = createString("boardTitle");

    public final StringPath boardType = createString("boardType");

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

    //inherited
    public final DateTimePath<java.sql.Timestamp> updateDate = _super.updateDate;

    //inherited
    public final NumberPath<Long> updateUser = _super.updateUser;

    public QBoard(String variable) {
        super(Board.class, forVariable(variable));
    }

    public QBoard(Path<? extends Board> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBoard(PathMetadata metadata) {
        super(Board.class, metadata);
    }

}

