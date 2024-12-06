package com.laoumri.socialmediaspringbackend.services;

import com.laoumri.socialmediaspringbackend.entities.FriendRequest;

import java.util.List;
import java.util.UUID;

public interface RequestService {
    String sendRequest(Long userId);
    String cancelRequest(UUID requestId);
    String acceptRequest(UUID requestId);
    String unfriend(Long userId);
    List<FriendRequest> getFriendRequests();
    List<FriendRequest> getSentRequests();
}
