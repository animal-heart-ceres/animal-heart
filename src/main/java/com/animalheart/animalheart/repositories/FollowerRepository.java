package com.animalheart.animalheart.repositories;

import com.animalheart.animalheart.models.Follower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FollowerRepository extends JpaRepository<Follower, Long> {
    Follower findByFollowerId(Long id);
}
