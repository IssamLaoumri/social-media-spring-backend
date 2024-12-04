package com.laoumri.socialmediaspringbackend.repositories;

import com.laoumri.socialmediaspringbackend.entities.Reactable;
import com.laoumri.socialmediaspringbackend.entities.Reaction;
import com.laoumri.socialmediaspringbackend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Integer> {
    Optional<Reaction> findByReactableAndReactedBy(Reactable reactable, User user);
    boolean existsByReactableAndReactedBy(Reactable reactable, User user);
}
