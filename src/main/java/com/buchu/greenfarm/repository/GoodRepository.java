package com.buchu.greenfarm.repository;

import com.buchu.greenfarm.entity.FarmLog;
import com.buchu.greenfarm.entity.Good;
import com.buchu.greenfarm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GoodRepository extends JpaRepository<Good, Long> {
    public Optional<List<Good>> findByFarmLog(FarmLog farmLog);
    public List<Good> findByLikerOrderByCreatedAtDesc(User user);
    public Optional<Good> findByLikerAndFarmLog(User liker, FarmLog farmLog);
    public Long countByFarmLog(FarmLog farmLog);
}
