package com.laoumri.socialmediaspringbackend.controllers;

import com.laoumri.socialmediaspringbackend.entities.FriendRequest;
import com.laoumri.socialmediaspringbackend.services.RequestService;
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
public class RequestController {
    private final RequestService requestService;

    @MutationMapping(name = "sendRequest")
    public String sendRequest(@Argument(name = "userId") Long userId) {
        return requestService.sendRequest(userId);
    }

    @MutationMapping(name = "cancelRequest")
    public String cancelRequest(@Argument(name = "requestId") UUID requestId) {
        return requestService.cancelRequest(requestId);
    }

    @MutationMapping(name = "acceptRequest")
    public String acceptRequest(@Argument(name = "requestId") UUID requestId) {
        return requestService.acceptRequest(requestId);
    }

    @MutationMapping(name = "unfriend")
    public String unfriend(@Argument(name = "userId") Long userId) {
        return requestService.unfriend(userId);
    }

    @QueryMapping
    public List<FriendRequest> getFriendRequests() {
        return requestService.getFriendRequests();
    }

    @QueryMapping
    public List<FriendRequest> getSentRequests() {
        return requestService.getSentRequests();
    }
}
