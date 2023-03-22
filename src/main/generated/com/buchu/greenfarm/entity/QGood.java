package com.buchu.greenfarm.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGood is a Querydsl query type for Good
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGood extends EntityPathBase<Good> {

    private static final long serialVersionUID = -410067787L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGood good = new QGood("good");

    public final QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final QFarmLog farmLog;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QUser liker;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QGood(String variable) {
        this(Good.class, forVariable(variable), INITS);
    }

    public QGood(Path<? extends Good> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QGood(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QGood(PathMetadata metadata, PathInits inits) {
        this(Good.class, metadata, inits);
    }

    public QGood(Class<? extends Good> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.farmLog = inits.isInitialized("farmLog") ? new QFarmLog(forProperty("farmLog"), inits.get("farmLog")) : null;
        this.liker = inits.isInitialized("liker") ? new QUser(forProperty("liker")) : null;
    }

}

