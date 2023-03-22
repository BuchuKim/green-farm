package com.buchu.greenfarm.repository;


import com.buchu.greenfarm.entity.FarmLog;
import com.buchu.greenfarm.entity.User;

import java.util.List;

public interface FarmLogCustomRepository {
    List<FarmLog> findFollowingFarmLogsUsingQueryDsl(User user);

    List<FarmLog> findByLikerUsingQueryDsl(User liker);
}
