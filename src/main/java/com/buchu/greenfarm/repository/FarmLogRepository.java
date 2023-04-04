package com.buchu.greenfarm.repository;

import com.buchu.greenfarm.entity.FarmLog;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FarmLogRepository extends JpaRepository<FarmLog,Long>, FarmLogCustomRepository {
    PageImpl<FarmLog> findByLogContentContaining(String keyword, Pageable pageable);
}
