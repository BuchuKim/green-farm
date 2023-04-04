package com.buchu.greenfarm.repository;


import com.buchu.greenfarm.entity.FarmLog;
import com.buchu.greenfarm.entity.User;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public interface FarmLogCustomRepository {

    PageImpl<FarmLog> findAllFarmLogsQueryDslPaging(Pageable pageable);

    PageImpl<FarmLog> findFollowingFarmLogsQueryDslPaging(Pageable pageable, User following);

    PageImpl<FarmLog> findByAuthorQueryDslPaging(User author, Pageable pageable);

    PageImpl<FarmLog> findByLikerQueryDslPaging(User liker, Pageable pageable);

    PageImpl<FarmLog> findByKeyWordQueryDslPaging(String keyword, Pageable pageable);
}
