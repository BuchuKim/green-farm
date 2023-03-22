package com.buchu.greenfarm.repository;

import com.buchu.greenfarm.entity.*;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FarmLogCustomRepositoryImpl implements FarmLogCustomRepository{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<FarmLog> findFollowingFarmLogsUsingQueryDsl(User follower) {
        QFarmLog farmLog = QFarmLog.farmLog;
        QFollow follow = QFollow.follow;

        return jpaQueryFactory
                .selectFrom(farmLog)
                .leftJoin(farmLog.likers).fetchJoin()
                .where(farmLog.author.in(
                        JPAExpressions.select(follow.followed)
                                .from(follow)
                                .where(follow.following.eq(follower))
                ).or(farmLog.author.eq(follower)))
                .orderBy(farmLog.createdAt.desc())
                .fetch();
    }

    @Override
    public List<FarmLog> findByLikerUsingQueryDsl(User liker) {
        QFarmLog farmLog = QFarmLog.farmLog;
        QGood good = QGood.good;
        return jpaQueryFactory
                .selectFrom(farmLog)
                .leftJoin(farmLog.likers).fetchJoin()
                .where(farmLog.in(JPAExpressions
                        .select(good.farmLog).from(good)
                        .where(good.liker.eq(liker))
                )).orderBy(farmLog.createdAt.desc())
                .fetch();
    }
}
