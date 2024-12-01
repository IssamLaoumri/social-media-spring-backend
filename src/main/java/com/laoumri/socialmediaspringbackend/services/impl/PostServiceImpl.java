package com.laoumri.socialmediaspringbackend.services.impl;

import com.laoumri.socialmediaspringbackend.dto.CreatePostRequest;
import com.laoumri.socialmediaspringbackend.dto.MediaRequest;
import com.laoumri.socialmediaspringbackend.entities.Media;
import com.laoumri.socialmediaspringbackend.entities.Post;
import com.laoumri.socialmediaspringbackend.entities.User;
import com.laoumri.socialmediaspringbackend.enums.EMedia;
import com.laoumri.socialmediaspringbackend.enums.EPost;
import com.laoumri.socialmediaspringbackend.enums.EPostVisibility;
import com.laoumri.socialmediaspringbackend.exceptions.InvalidRequestException;
import com.laoumri.socialmediaspringbackend.exceptions.ResourceNotFoundException;
import com.laoumri.socialmediaspringbackend.exceptions.UnauthorizedException;
import com.laoumri.socialmediaspringbackend.repositories.PostRepository;
import com.laoumri.socialmediaspringbackend.repositories.UserRepository;
import com.laoumri.socialmediaspringbackend.services.PostService;
import com.laoumri.socialmediaspringbackend.utils.SecurityUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public String createPost(CreatePostRequest request) {
        // Get Authenticated User
        User currentUser = SecurityUtility.getCurrentUser();

        // Validate the request if the update is for profile banners
        validate(request);

        // Create Media
        List<Media> media = new ArrayList<>();
        for(MediaRequest m : request.getMedia()){
            Media item = Media.builder()
                    .url(m.getUrl())
                    .type(EMedia.valueOf(m.getType()))
                    .build();
            media.add(item);
        }

        // Create the Post
        Post post = Post.builder()
                .type(EPost.valueOf(request.getType()))
                .user(currentUser)
                .content(request.getContent())
                .media(media)
                .publishedAt(Instant.now())
                .visibility(EPostVisibility.valueOf(request.getVisibility()))
                .build();

        postRepository.save(post);
        return "POST_CREATE_SUCCESS";
    }

    @Override
    public String updatePost(UUID id, CreatePostRequest request) {
        // Get Authenticated User
        User currentUser = SecurityUtility.getCurrentUser();

        // Fetch the existing post
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("POST_NOT_FOUND"));

        // Check if the authenticated user is the owner of the post
        if (!existingPost.getUser().getId().equals(currentUser.getId())) throw new UnauthorizedException("UNAUTHORIZED");

        // Validate the request if the update is for profile banners
        validate(request);

        // Update Media
        List<Media> existingMedia = existingPost.getMedia();
        for (MediaRequest m : request.getMedia()) {
            Media item = Media.builder()
                    .url(m.getUrl())
                    .type(EMedia.valueOf(m.getType()))
                    .build();
            existingMedia.add(item);
        }

        // Update Post details
        existingPost.setType(EPost.valueOf(request.getType()));
        existingPost.setContent(request.getContent());
        existingPost.setMedia(existingMedia);
        existingPost.setVisibility(EPostVisibility.valueOf(request.getVisibility()));
        existingPost.setPublishedAt(Instant.now());

        // Save the updated post
        postRepository.save(existingPost);

        return "POST_UPDATE_SUCCESS";
    }


    @Override
    public String deletePost(UUID id) {
        // Get Authenticated User
        User currentUser = SecurityUtility.getCurrentUser();

        // Fetch the existing post
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("POST_NOT_FOUND"));

        // Check if the authenticated user is the owner of the post
        if (!existingPost.getUser().getId().equals(currentUser.getId())) throw new UnauthorizedException("UNAUTHORIZED");

        postRepository.delete(existingPost);
        return "POST_DELETE_SUCCESS";
    }

    @Override
    public Post getPostById(UUID id) {
        // Get Authenticated User
        User currentUser = userRepository.findById(SecurityUtility.getCurrentUser().getId())
                .orElseThrow(() -> new InternalAuthenticationServiceException("SOMETHING_WENT_WRONG"));

        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("POST_NOT_FOUND"));
        User postOwner = existingPost.getUser();

        if(currentUser.getId().equals(postOwner.getId())){
            return existingPost;
        }

        switch (existingPost.getVisibility()) {
            case PUBLIC -> {
                return existingPost;
            }
            case PRIVATE -> {
                throw new UnauthorizedException("UNAUTHORIZED");
            }
            case FRIENDS -> {
                if(currentUser.getFriends().contains(existingPost.getUser()) && existingPost.getUser().getFriends().contains(currentUser)){
                    return existingPost;
                }
                throw new UnauthorizedException("UNAUTHORIZED");
            }
        }
        return null;
    }

    @Override
    public List<Post> getAllCurrentUserPosts() {
        // Get Authenticated User
        User currentUser = SecurityUtility.getCurrentUser();
        return postRepository.findByUserId(currentUser.getId());
    }

    @Override
    public List<Post> getAllUserPosts(Long userId) {
        // Get Authenticated User
        User currentUser = userRepository.findById(SecurityUtility.getCurrentUser().getId())
                .orElseThrow(() -> new InternalAuthenticationServiceException("SOMETHING_WENT_WRONG"));
        List<Post> posts = postRepository.findByUserId(userId);
        List<Post> filteredPosts = new ArrayList<>();
        for(Post post : posts){
            switch (post.getVisibility()) {
                case PUBLIC -> filteredPosts.add(post);
                case PRIVATE -> {
                }
                case FRIENDS -> {
                    if(currentUser.getFriends().contains(post.getUser()) && post.getUser().getFriends().contains(currentUser)){
                        filteredPosts.add(post);
                    }
                }
            }
        }
        return filteredPosts;
    }

    private static void validate(CreatePostRequest request) {
        Set<EPost> profileBanners = EnumSet.of(EPost.POST_PROFILE_PICTURE, EPost.POST_COVER_PICTURE);
        if (profileBanners.contains(EPost.valueOf(request.getType()))) {
            List<MediaRequest> media = request.getMedia();
            if (media.size() != 1 || !media.get(0).getType().equals(EMedia.PHOTO.name())) {
                throw new InvalidRequestException("INVALID_REQUEST");
            }
        }
    }
}
