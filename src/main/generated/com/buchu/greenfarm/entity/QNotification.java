package com.buchu.greenfarm.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QNotification is a Querydsl query type for Notification
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNotification extends EntityPathBase<Notification> {

    private static final long serialVersionUID = -1030132509L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QNotification notification = new QNotification("notification");

    public final QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final QFarmLog farmLog;

    public final BooleanPath isRead = createBoolean("isRead");

    public final StringPath message = createString("message");

    public final EnumPath<com.buchu.greenfarm.code.NotificationCode> notificationCode = createEnum("notificationCode", com.buchu.greenfarm.code.NotificationCode.class);

    public final NumberPath<Long> notificationId = createNumber("notificationId", Long.class);

    public final QUser receivingUser;

    public final QUser sendingUser;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QNotification(String variable) {
        this(Notification.class, forVariable(variable), INITS);
    }

    public QNotification(Path<? extends Notification> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QNotification(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QNotification(PathMetadata metadata, PathInits inits) {
        this(Notification.class, metadata, inits);
    }

    public QNotification(Class<? extends Notification> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.farmLog = inits.isInitialized("farmLog") ? new QFarmLog(forProperty("farmLog"), inits.get("farmLog")) : null;
        this.receivingUser = inits.isInitialized("receivingUser") ? new QUser(forProperty("receivingUser")) : null;
        this.sendingUser = inits.isInitialized("sendingUser") ? new QUser(forProperty("sendingUser")) : null;
    }

}

