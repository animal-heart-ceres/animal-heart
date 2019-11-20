package com.animalheart.animalheart.repositories;

import com.animalheart.animalheart.models.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    UserProfile findByFirstName(String firstName);
}
