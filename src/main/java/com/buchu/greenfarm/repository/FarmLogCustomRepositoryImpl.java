package com.buchu.greenfarm.repository;

import com.buchu.greenfarm.entity.*;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FarmLogCustomRepositoryImpl implements FarmLogCustomRepository{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public PageImpl<FarmLog> findAllFarmLogsQueryDslPaging(Pageable pageable) {
        QFarmLog farmLog = QFarmLog.farmLog;

        // limit 절만 사용할 query 따로 뺌
        List<Long> farmLogIds = jpaQueryFactory
                .select(farmLog.farmLogId)
                .from(farmLog)
                .orderBy(farmLog.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // fetchJoin
        List<FarmLog> farmLogs = jpaQueryFactory.selectFrom(farmLog)
                .leftJoin(farmLog.likers).fetchJoin()
                .where(farmLog.farmLogId.in(farmLogIds))
                .orderBy(farmLog.createdAt.desc())
                .fetch();

        Long count = jpaQueryFactory.select(farmLog.count()).from(farmLog).fetchOne();
        return new PageImpl<>(farmLogs,pageable,(count==null) ? 0 : count);
    }

    @Override
    public PageImpl<FarmLog> findFollowingFarmLogsQueryDslPaging(
            final Pageable pageable,
            final User follower) {
        QFarmLog farmLog = QFarmLog.farmLog;
        QFollow follow = QFollow.follow;

        JPAQuery<Long> followingQuery = jpaQueryFactory.select(farmLog.farmLogId)
                .from(farmLog)
                .where(farmLog.author.in(
                        JPAExpressions.select(follow.followed)
                                .from(follow)
                                .where(follow.following.eq(follower))
                ).or(farmLog.author.eq(follower)))
                .orderBy(farmLog.createdAt.desc());

        long count = followingQuery.stream().count();

        List<FarmLog> followingFarmLogs = jpaQueryFactory
                .selectFrom(farmLog)
                .leftJoin(farmLog.likers).fetchJoin()
                .where(farmLog.farmLogId.in(
                        followingQuery
                                .offset(pageable.getOffset())
                                .limit(pageable.getPageSize())
                                .fetch()))
                .orderBy(farmLog.createdAt.desc())
                .fetch();

        return new PageImpl<>(followingFarmLogs,
                pageable,
                count);
    }

    @Override
    public PageImpl<FarmLog> findByAuthorQueryDslPaging(
            final User author,
            final Pageable pageable) {
        QFarmLog farmLog = QFarmLog.farmLog;

        List<Long> farmLogIds = jpaQueryFactory
                .select(farmLog.farmLogId)
                .from(farmLog)
                .orderBy(farmLog.createdAt.desc())
                .where(farmLog.author.eq(author))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<FarmLog> farmLogs = jpaQueryFactory
                .selectFrom(farmLog)
                .where(farmLog.farmLogId.in(
                        farmLogIds))
                .leftJoin(farmLog.likers).fetchJoin()
                .orderBy(farmLog.createdAt.desc())
                .fetch();

        Long count = jpaQueryFactory
                .select(farmLog.count())
                .from(farmLog)
                .where(farmLog.author.eq(author))
                .fetchOne();

        return new PageImpl<>(farmLogs, pageable, count==null ? 0 : count);
    }

    @Override
    public PageImpl<FarmLog> findByLikerQueryDslPaging(final User liker,
                                                       final Pageable pageable) {
        QFarmLog farmLog = QFarmLog.farmLog;
        QGood good = QGood.good;

        List<Long> farmLogIds = jpaQueryFactory
                .select(farmLog.farmLogId).from(farmLog)
                .where(farmLog.in(JPAExpressions
                                .select(good.farmLog).from(good)
                                .where(good.liker.eq(liker))))
                .orderBy(farmLog.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<FarmLog> farmLogs = jpaQueryFactory
                .selectFrom(farmLog)
                .leftJoin(farmLog.likers).fetchJoin()
                .where(farmLog.farmLogId.in(
                        farmLogIds))
                .orderBy(farmLog.createdAt.desc())
                .fetch();

        Long count = jpaQueryFactory
                .select(farmLog.count())
                .from(farmLog)
                .where(farmLog.in(
                        JPAExpressions
                                .select(good.farmLog).from(good)
                                .where(good.liker.eq(liker))))
                .fetchOne();

        return new PageImpl<>(farmLogs, pageable, count == null ? 0 : count);
    }
}
