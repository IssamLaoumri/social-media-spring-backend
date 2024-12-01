package com.laoumri.socialmediaspringbackend.services;

import com.laoumri.socialmediaspringbackend.dto.requests.CreatePostRequest;
import com.laoumri.socialmediaspringbackend.entities.Post;

import java.util.List;
import java.util.UUID;

public interface PostService {
    String createPost(CreatePostRequest request);
    String updatePost(UUID id, CreatePostRequest request);
    String deletePost(UUID id);
    Post getPostById(UUID id);
    List<Post> getAllCurrentUserPosts();
    List<Post> getAllUserPosts(Long userId);
}
