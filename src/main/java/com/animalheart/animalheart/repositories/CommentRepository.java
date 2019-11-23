package com.animalheart.animalheart.repositories;

import com.animalheart.animalheart.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByComment(String comment);
}
