package com.animalheart.animalheart.repositories;

import com.animalheart.animalheart.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Comment findByComment(String comment);
}
