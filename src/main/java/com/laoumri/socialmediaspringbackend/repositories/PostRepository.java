package com.laoumri.socialmediaspringbackend.repositories;

import com.laoumri.socialmediaspringbackend.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
    @Query("""
    SELECT DISTINCT p
    FROM Post p
    LEFT JOIN FETCH p.comments c
    WHERE p.user.id = :userId AND (c.parent IS NULL OR c IS NULL)
    """)
    List<Post> findPostsByUserIdWithTopLevelComments(Long userId);
    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.comments c WHERE p.id = :postId AND c.parent IS NULL")
    Optional<Post> findPostByIdWithTopLevelComments(UUID postId);

}
