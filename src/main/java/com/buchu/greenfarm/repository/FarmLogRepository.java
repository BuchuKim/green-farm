package com.buchu.greenfarm.repository;

import com.buchu.greenfarm.code.FarmLogStatusCode;
import com.buchu.greenfarm.entity.FarmLog;
import com.buchu.greenfarm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FarmLogRepository extends JpaRepository<FarmLog,Long> {
    List<FarmLog> getByFarmLogStatusCodeOrderByCreatedAtDesc(FarmLogStatusCode farmLogStatusCode);
    List<FarmLog> getByAuthorOrderByCreatedAtDesc(User author);
    List<FarmLog> getByAuthor(User author);
}
