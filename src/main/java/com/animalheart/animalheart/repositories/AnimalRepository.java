package com.animalheart.animalheart.repositories;

import com.animalheart.animalheart.models.Animal;
import com.animalheart.animalheart.models.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnimalRepository extends JpaRepository<Animal, Long> {
    Animal findByName(String name);
}
