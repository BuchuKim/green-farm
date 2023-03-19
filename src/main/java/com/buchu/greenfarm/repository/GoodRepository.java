package com.buchu.greenfarm.repository;

import com.buchu.greenfarm.entity.FarmLog;
import com.buchu.greenfarm.entity.Good;
import com.buchu.greenfarm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GoodRepository extends JpaRepository<Good, Long> {
    @Query("select g " +
            "from Good g " +
            "left join fetch g.farmLog " +
            "where g.liker = :user " +
            "order by g.createdAt desc")
    public List<Good> findByLikerOrderByCreatedAtDesc(@Param("user") User user);

    public Optional<Good> findByLikerAndFarmLog(User liker, FarmLog farmLog);
    public int countByFarmLog(FarmLog farmLog);
}
