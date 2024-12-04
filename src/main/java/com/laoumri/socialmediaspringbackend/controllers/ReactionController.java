package com.laoumri.socialmediaspringbackend.controllers;

import com.laoumri.socialmediaspringbackend.services.ReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ReactionController {
    private final ReactionService reactionService;

    @MutationMapping
    public String addReaction(@Argument UUID reactableId,@Argument String reactableType,@Argument String reaction){
        return reactionService.addReaction(reactableId, reactableType, reaction);
    }

    @MutationMapping
    public String deleteReaction(@Argument UUID reactableId,@Argument String reactableType){
        return reactionService.deleteReaction(reactableId, reactableType);
    }
}
