package com.laoumri.socialmediaspringbackend.repositories;

import com.laoumri.socialmediaspringbackend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    boolean existsBy_username(String username);
    Optional<User> findByEmail(String email);
}
