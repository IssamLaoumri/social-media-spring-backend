package com.laoumri.socialmediaspringbackend.controllers;

import com.laoumri.socialmediaspringbackend.dto.requests.CreatePostRequest;
import com.laoumri.socialmediaspringbackend.entities.Post;
import com.laoumri.socialmediaspringbackend.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class PostController {
    private final PostService postService;

    @MutationMapping(name = "createPost")
    public String createPost(@Argument(name = "input") CreatePostRequest request){
        return postService.createPost(request);
    }

    @MutationMapping(name = "updatePost")
    public String updatePost(@Argument(name = "postId") UUID id, @Argument(name = "input") CreatePostRequest request){
        return postService.updatePost(id, request);
    }

    @MutationMapping(name = "deletePost")
    public String deletePost(@Argument(name = "postId") UUID id){
        return postService.deletePost(id);
    }

    @QueryMapping(name = "getPostById")
    public Post getPostById(@Argument(name = "postId") UUID id){
        return postService.getPostById(id);
    }

    @QueryMapping(name = "getAllCurrentUserPosts")
    public List<Post> getAllCurrentUserPosts(){
        return postService.getAllCurrentUserPosts();
    }

    @QueryMapping(name = "getAllUserPosts")
    public List<Post> getAllUserPosts(@Argument(name = "userId") Long userId){
        return postService.getAllUserPosts(userId);
    }
}
