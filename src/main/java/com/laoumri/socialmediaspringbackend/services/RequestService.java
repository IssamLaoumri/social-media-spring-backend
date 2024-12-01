package com.laoumri.socialmediaspringbackend.services;

import java.util.UUID;

public interface RequestService {
    String sendRequest(Long userId);
    String cancelRequest(UUID requestId);
    String acceptRequest(UUID requestId);
    String unfriend(Long userId);
}
