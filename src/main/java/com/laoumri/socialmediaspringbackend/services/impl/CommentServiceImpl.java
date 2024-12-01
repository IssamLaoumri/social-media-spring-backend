package com.laoumri.socialmediaspringbackend.services.impl;

import com.laoumri.socialmediaspringbackend.dto.CommentRequest;
import com.laoumri.socialmediaspringbackend.dto.MediaRequest;
import com.laoumri.socialmediaspringbackend.entities.Comment;
import com.laoumri.socialmediaspringbackend.entities.Media;
import com.laoumri.socialmediaspringbackend.entities.Post;
import com.laoumri.socialmediaspringbackend.entities.User;
import com.laoumri.socialmediaspringbackend.enums.EMedia;
import com.laoumri.socialmediaspringbackend.exceptions.ResourceNotFoundException;
import com.laoumri.socialmediaspringbackend.exceptions.UnauthorizedException;
import com.laoumri.socialmediaspringbackend.repositories.CommentRepository;
import com.laoumri.socialmediaspringbackend.repositories.MediaRepository;
import com.laoumri.socialmediaspringbackend.repositories.PostRepository;
import com.laoumri.socialmediaspringbackend.repositories.UserRepository;
import com.laoumri.socialmediaspringbackend.services.CommentService;
import com.laoumri.socialmediaspringbackend.utils.SecurityUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final MediaRepository mediaRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public String addComment(UUID postId,CommentRequest request) {
        // Get authenticated user
        User currentUser = userRepository.findById(SecurityUtility.getCurrentUser().getId())
                .orElseThrow(() -> new InternalAuthenticationServiceException("SOMETHING_WENT_WRONG"));

        // Get related post
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("POST_NOT_FOUND"));
        // What if the user try to comment on a post he is not allowed to
        switch (post.getVisibility()){
            case PRIVATE -> throw new UnauthorizedException("UNAUTHORIZED");
            case FRIENDS -> {
                if(!currentUser.getFriends().contains(post.getUser()) || !post.getUser().getFriends().contains(currentUser)){
                    throw new UnauthorizedException("UNAUTHORIZED");
                }
            }
        }
        // Create and save the media and the comment
        Media media = null;
        if(request.getMedia() != null){
            media = Media.builder()
                    .url(request.getMedia().getUrl())
                    .type(EMedia.valueOf(request.getMedia().getType()))
                    .build();
        }

        Comment comment = Comment.builder()
                .commentBy(currentUser)
                .commentedAt(Instant.now())
                .content(request.getContent())
                .post(post)
                .media(media)
                .build();
        commentRepository.save(comment);
        return "COMMENT_SUCCESS";
    }

    @Override
    public String deleteComment(UUID commentId) {
        // Get Authenticated User
        User currentUser = SecurityUtility.getCurrentUser();
        // Get The comment
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("COMMENT_NOT_FOUND"));
        // Check if the comment does not belong to the current User nor the owner of the post
        if(!comment.getCommentBy().getId().equals(currentUser.getId())
            && !comment.getPost().getUser().getId().equals(currentUser.getId())
        ){
            throw new UnauthorizedException("UNAUTHORIZED");
        }
        // Allow Delete
        commentRepository.delete(comment);
        return "COMMENT_DELETE_SUCCESS";
    }

    @Override
    public String updateComment(UUID commentId, CommentRequest request) {
        // Get authenticated user
        User currentUser = SecurityUtility.getCurrentUser();
        // Get the comment
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("COMMENT_NOT_FOUND"));
        // Check if the comment belong to the current user
        if(!comment.getCommentBy().getId().equals(currentUser.getId())){
            throw new UnauthorizedException("UNAUTHORIZED");
        }
        // Update
        if (request.getMedia() != null) {
            MediaRequest newMediaRequest = request.getMedia();
            if (comment.getMedia() != null) {
                mediaRepository.delete(comment.getMedia());
            }
            // Add the new media
            Media newMedia = Media.builder()
                    .type(EMedia.valueOf(newMediaRequest.getType()))
                    .url(newMediaRequest.getUrl())
                    .build();
            comment.setMedia(newMedia);
        }
        comment.setContent(request.getContent());
        comment.setCommentedAt(Instant.now());
        commentRepository.save(comment);
        return "COMMENT_UPDATE_SUCCESS";
    }
}
