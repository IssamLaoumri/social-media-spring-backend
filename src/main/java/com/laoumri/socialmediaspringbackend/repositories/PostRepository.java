package com.laoumri.socialmediaspringbackend.repositories;

import com.laoumri.socialmediaspringbackend.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    List<Post> findByUserId(Long userId);
}
