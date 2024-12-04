package com.laoumri.socialmediaspringbackend.services;

import java.util.UUID;

public interface ReactionService {
    String addReaction(UUID reactableId, String reactableType, String reaction);
    String deleteReaction(UUID reactableId, String reactableType);
}
