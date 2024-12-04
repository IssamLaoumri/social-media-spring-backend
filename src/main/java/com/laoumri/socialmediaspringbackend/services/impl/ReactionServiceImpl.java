package com.laoumri.socialmediaspringbackend.services.impl;

import com.laoumri.socialmediaspringbackend.entities.*;
import com.laoumri.socialmediaspringbackend.enums.EReact;
import com.laoumri.socialmediaspringbackend.exceptions.ResourceNotFoundException;
import com.laoumri.socialmediaspringbackend.exceptions.UnauthorizedException;
import com.laoumri.socialmediaspringbackend.repositories.ReactionRepository;
import com.laoumri.socialmediaspringbackend.repositories.UserRepository;
import com.laoumri.socialmediaspringbackend.services.ReactionService;
import com.laoumri.socialmediaspringbackend.utils.SecurityUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReactionServiceImpl implements ReactionService {

    private final ReactionRepository reactionRepository;
    private final UserRepository userRepository;

    private final ReactableService reactableService;

    @Override
    public String addReaction(UUID reactableId, String reactableType, String reaction) {
        Reactable reactable = reactableService.findReactable(reactableId, reactableType);
        User currentUser = userRepository.findById(SecurityUtility.getCurrentUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND"));

        // What if the user already reacted ? (to avoid duplicates) delete the reaction and let it be updated
        if(reactionRepository.existsByReactableAndReactedBy(reactable, currentUser)){
            Reaction react = reactionRepository.findByReactableAndReactedBy(reactable, currentUser)
                    .orElseThrow(() -> new ResourceNotFoundException("REACTION_NOT_FOUND"));

            reactionRepository.delete(react);
        }

        // Authorization logic for visibility
        validateUserCanReact(reactable, currentUser);

        // Create and save the reaction
        Reaction reactionToAdd = Reaction.builder()
                .react(EReact.valueOf(reaction))
                .reactable(reactable)
                .reactedBy(currentUser)
                .build();

        reactionRepository.save(reactionToAdd);
        return "REACTION_ADDED";
    }

    @Override
    public String deleteReaction(UUID reactableId, String reactableType) {
        Reactable reactable = reactableService.findReactable(reactableId, reactableType);
        User currentUser = userRepository.findById(SecurityUtility.getCurrentUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND"));

        Reaction reaction = reactionRepository.findByReactableAndReactedBy(reactable, currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("REACTION_NOT_FOUND"));

        reactionRepository.delete(reaction);
        return "REACTION_DELETED";
    }

    private void validateUserCanReact(Reactable reactable, User currentUser) {
        if (reactable instanceof Post post) {
            switch (post.getVisibility()) {
                case PRIVATE -> throw new UnauthorizedException("UNAUTHORIZED");
                case FRIENDS -> {
                    if (!currentUser.getFriends().contains(post.getUser()) ||
                            !post.getUser().getFriends().contains(currentUser)) {
                        throw new UnauthorizedException("UNAUTHORIZED");
                    }
                }
            }
        } else if (reactable instanceof Comment comment) {
            Post relatedPost = comment.getPost();
            if (relatedPost != null) {
                validateUserCanReact(relatedPost, currentUser); // Reuse logic for posts
            }
        }
    }

}

