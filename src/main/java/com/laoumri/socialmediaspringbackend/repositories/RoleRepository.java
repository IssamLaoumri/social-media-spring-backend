package com.laoumri.socialmediaspringbackend.repositories;

import com.laoumri.socialmediaspringbackend.entities.Role;
import com.laoumri.socialmediaspringbackend.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERole name);
}
