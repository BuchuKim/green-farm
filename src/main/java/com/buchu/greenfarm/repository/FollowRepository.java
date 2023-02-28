package com.buchu.greenfarm.repository;

import com.buchu.greenfarm.entity.Follow;
import com.buchu.greenfarm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow,Long> {
    public List<Follow> findByFollowed(User followed);
    public List<Follow> findByFollowing(User following);
    public Optional<Follow> findByFollowingAndFollowed(User following, User followed);
}
