package com.animalheart.animalheart.repositories;

import com.animalheart.animalheart.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Users extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
