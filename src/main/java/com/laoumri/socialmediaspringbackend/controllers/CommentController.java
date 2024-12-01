package com.laoumri.socialmediaspringbackend.controllers;

import com.laoumri.socialmediaspringbackend.dto.CommentRequest;
import com.laoumri.socialmediaspringbackend.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @MutationMapping
    public String addComment(@Argument UUID postId, @Argument CommentRequest request){
        return commentService.addComment(postId, request);
    }

    @MutationMapping
    public String deleteComment(@Argument UUID commentId){
        return commentService.deleteComment(commentId);
    }

    @MutationMapping
    public String updateComment(@Argument UUID commentId, @Argument CommentRequest request){
        return commentService.updateComment(commentId, request);
    }
}
