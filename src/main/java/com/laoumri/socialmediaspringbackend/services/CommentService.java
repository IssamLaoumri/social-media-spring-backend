package com.laoumri.socialmediaspringbackend.services;

import com.laoumri.socialmediaspringbackend.dto.CommentRequest;

import java.util.UUID;

public interface CommentService {
    String addCommentToPost(UUID postId, CommentRequest request);
    String deleteComment(UUID commentId);
    String updateComment(UUID commentId, CommentRequest request);
    String addReplyToComment(UUID commentId, CommentRequest request);
}
