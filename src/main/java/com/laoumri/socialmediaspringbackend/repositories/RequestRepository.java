package com.laoumri.socialmediaspringbackend.repositories;

import com.laoumri.socialmediaspringbackend.entities.FriendRequest;
import com.laoumri.socialmediaspringbackend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RequestRepository extends JpaRepository<FriendRequest, UUID> {
    Optional<FriendRequest> findByRequesterAndRequested(User requester, User requested);
    List<FriendRequest> findByRequested(User requested);
    List<FriendRequest> findByRequester(User requester);
}
