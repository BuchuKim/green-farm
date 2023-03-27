package com.buchu.greenfarm.repository;

import com.buchu.greenfarm.entity.FarmLog;
import com.buchu.greenfarm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FarmLogRepository extends JpaRepository<FarmLog,Long>, FarmLogCustomRepository {

    @Query("select f " +
            "from FarmLog f " +
            "left join fetch f.likers " +
            "where f.author = :author " +
            "order by f.createdAt desc")
    List<FarmLog> findByAuthorOrderByCreatedAtDesc(@Param("author") User author);
}
