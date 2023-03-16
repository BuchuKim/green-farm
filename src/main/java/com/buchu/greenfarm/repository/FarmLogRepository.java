package com.buchu.greenfarm.repository;

import com.buchu.greenfarm.entity.FarmLog;
import com.buchu.greenfarm.entity.User;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FarmLogRepository extends JpaRepository<FarmLog,Long> {
    @Query("select f " +
            "from FarmLog f " +
            "left join fetch f.likers " +
            "order by f.createdAt desc")
    List<FarmLog> findAllForIndex();

    @Query("select f " +
            "from FarmLog f " +
            "join fetch f.likers " +
            "where f.author = :author " +
            "order by f.createdAt desc")
    List<FarmLog> findByAuthorOrderByCreatedAtDesc(@Param("author") User author);

    List<FarmLog> findByAuthor(User author);
}
