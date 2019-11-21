package com.animalheart.animalheart.repositories;

import com.animalheart.animalheart.models.Follower;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowerRepository extends JpaRepository<Follower, Long> {
    Follower findByFollowerId(Long id);

    List<Follower> findFollowersByFollowerId(Long id);
}
