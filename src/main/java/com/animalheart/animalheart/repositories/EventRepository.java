package com.animalheart.animalheart.repositories;

import com.animalheart.animalheart.models.Animal;
import com.animalheart.animalheart.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
    Event findByTitle(String title);
}
