package com.laoumri.socialmediaspringbackend.repositories;

import com.laoumri.socialmediaspringbackend.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
}
