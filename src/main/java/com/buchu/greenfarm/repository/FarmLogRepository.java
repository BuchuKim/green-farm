package com.buchu.greenfarm.repository;

import com.buchu.greenfarm.entity.FarmLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FarmLogRepository extends JpaRepository<FarmLog,Long>, FarmLogCustomRepository {
}
