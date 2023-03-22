package com.buchu.greenfarm.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFarmLog is a Querydsl query type for FarmLog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFarmLog extends EntityPathBase<FarmLog> {

    private static final long serialVersionUID = 1567318070L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFarmLog farmLog = new QFarmLog("farmLog");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final QUser author;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> farmLogId = createNumber("farmLogId", Long.class);

    public final ListPath<Good, QGood> likers = this.<Good, QGood>createList("likers", Good.class, QGood.class, PathInits.DIRECT2);

    public final StringPath logContent = createString("logContent");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QFarmLog(String variable) {
        this(FarmLog.class, forVariable(variable), INITS);
    }

    public QFarmLog(Path<? extends FarmLog> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFarmLog(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFarmLog(PathMetadata metadata, PathInits inits) {
        this(FarmLog.class, metadata, inits);
    }

    public QFarmLog(Class<? extends FarmLog> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.author = inits.isInitialized("author") ? new QUser(forProperty("author")) : null;
    }

}

