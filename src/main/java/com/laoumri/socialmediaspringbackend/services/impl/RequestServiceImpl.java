package com.laoumri.socialmediaspringbackend.services.impl;

import com.laoumri.socialmediaspringbackend.entities.FriendRequest;
import com.laoumri.socialmediaspringbackend.entities.User;
import com.laoumri.socialmediaspringbackend.exceptions.InvalidRequestException;
import com.laoumri.socialmediaspringbackend.exceptions.ResourceNotFoundException;
import com.laoumri.socialmediaspringbackend.repositories.RequestRepository;
import com.laoumri.socialmediaspringbackend.repositories.UserRepository;
import com.laoumri.socialmediaspringbackend.services.RequestService;
import com.laoumri.socialmediaspringbackend.utils.SecurityUtility;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    @Override
    public String sendRequest(Long userId) {
        // Get the current authenticated user
        User requester = userRepository.findById(SecurityUtility.getCurrentUser().getId())
                .orElseThrow(() -> new InternalAuthenticationServiceException("SOMETHING_WENT_WRONG"));


        // Check if the current user is sending a request to himself
        if(requester.getId().equals(userId)){
            throw new InvalidRequestException("INVALID_REQUEST");
        }

        // else, get the requested user
        User requested = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("USER_NOT_FOUND"));

        // If the requested user exists, then check if the requested and current user are friends
        if(requester.getFriends().contains(requested)){
            throw new InvalidRequestException("INVALID_REQUEST");
        }
        // Check if the request is already there as pending
        Optional<FriendRequest> existingRequest = requestRepository.findByRequesterAndRequested(requester, requested);
        Optional<FriendRequest> receiverExistingRequest = requestRepository.findByRequesterAndRequested(requested, requester);

        if(existingRequest.isPresent() || receiverExistingRequest.isPresent()){
            throw new InvalidRequestException("INVALID_REQUEST");
        }

        // If everything pass successfully, then save the request
        FriendRequest request = FriendRequest.builder()
                .requester(requester)
                .requested(requested)
                .requestedAt(Instant.now())
                .build();

        requestRepository.save(request);
        return "FRIEND_REQUEST_SENT";
    }

    @Override
    public String cancelRequest(UUID requestId) {
        // Get the request
        FriendRequest request = requestRepository.findById(requestId).orElseThrow(()-> new ResourceNotFoundException("REQUEST_NOT_FOUND"));

        // Get the authenticated User
        User currentUser = userRepository.findById(SecurityUtility.getCurrentUser().getId())
                .orElseThrow(() -> new InternalAuthenticationServiceException("SOMETHING_WENT_WRONG"));

        // check if the request belong to the current user and the requester/requested
        if(!request.getRequester().getId().equals(currentUser.getId()) && !request.getRequested().getId().equals(currentUser.getId())){
            throw new InvalidRequestException("INVALID_REQUEST");
        }

        // Check if the requester and requested are already friends
        if(request.getRequested().getFriends().contains(request.getRequester())){
            throw new InvalidRequestException("ALREADY_FRIENDS");
        }

        // if everything passes successfully, then delete the request
        requestRepository.delete(request);

        return "FRIEND_REQUEST_CANCELLED";
    }

    @Override
    public String acceptRequest(UUID requestId) {
        // Get the request
        FriendRequest request = requestRepository.findById(requestId).orElseThrow(()-> new ResourceNotFoundException("REQUEST_NOT_FOUND"));

        // Get the authenticated user
        User currentUser = userRepository.findById(SecurityUtility.getCurrentUser().getId())
                .orElseThrow(() -> new InternalAuthenticationServiceException("SOMETHING_WENT_WRONG"));

        // Check if the current user is the requested otherwise that's not allowed
        if(!currentUser.getId().equals(request.getRequested().getId())){
            throw new InvalidRequestException("INVALID_REQUEST");
        }

        // Check if the current user and the requester are friends
        if(currentUser.getFriends().contains(request.getRequester())){
            throw new InvalidRequestException("ALREADY_FRIENDS");
        }

        // if everything passes, the current user and the requester must be friends
        currentUser.getFriends().add(request.getRequester());
        request.getRequester().getFriends().add(currentUser);
        userRepository.saveAll(List.of(currentUser, request.getRequester()));

        // the request must be deleted
        requestRepository.delete(request);
        return "FRIEND_REQUEST_ACCEPTED";
    }

    @Override
    public String unfriend(Long userId) {
        // Get the authenticated User
        User currentUser = userRepository.findById(SecurityUtility.getCurrentUser().getId())
                .orElseThrow(() -> new InternalAuthenticationServiceException("SOMETHING_WENT_WRONG"));;
        // Get the friend user
        User friend = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("USER_NOT_FOUND"));
        // Check if the current user and the other user are friends
        if(!currentUser.getFriends().contains(friend) || !friend.getFriends().contains(currentUser)){
            throw new InvalidRequestException("INVALID_REQUEST");
        }
        // If yes, then unlink them
        currentUser.getFriends().remove(friend);
        friend.getFriends().remove(currentUser);
        userRepository.saveAll(List.of(currentUser, friend));
        return "UNFRIEND_SUCCESS";
    }
}
