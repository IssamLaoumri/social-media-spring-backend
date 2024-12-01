package com.laoumri.socialmediaspringbackend.repositories;

import com.laoumri.socialmediaspringbackend.entities.Media;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MediaRepository extends JpaRepository<Media, UUID> {
}
