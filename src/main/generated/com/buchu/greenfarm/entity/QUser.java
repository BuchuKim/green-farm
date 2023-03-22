package com.buchu.greenfarm.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -409647165L;

    public static final QUser user = new QUser("user");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final StringPath bio = createString("bio");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    public final ListPath<FarmLog, QFarmLog> farmLog = this.<FarmLog, QFarmLog>createList("farmLog", FarmLog.class, QFarmLog.class, PathInits.DIRECT2);

    public final ListPath<Follow, QFollow> followed = this.<Follow, QFollow>createList("followed", Follow.class, QFollow.class, PathInits.DIRECT2);

    public final ListPath<Follow, QFollow> following = this.<Follow, QFollow>createList("following", Follow.class, QFollow.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<Good, QGood> likeList = this.<Good, QGood>createList("likeList", Good.class, QGood.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    public final StringPath oAuth2Id = createString("oAuth2Id");

    public final StringPath phoneNum = createString("phoneNum");

    public final StringPath registrationId = createString("registrationId");

    public final EnumPath<Role> role = createEnum("role", Role.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final StringPath userId = createString("userId");

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

