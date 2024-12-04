package com.laoumri.socialmediaspringbackend.services.impl;

import com.laoumri.socialmediaspringbackend.entities.Reactable;
import com.laoumri.socialmediaspringbackend.exceptions.ResourceNotFoundException;
import com.laoumri.socialmediaspringbackend.repositories.CommentRepository;
import com.laoumri.socialmediaspringbackend.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReactableService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public Reactable findReactable(UUID id, String type) {
        return switch (type.toLowerCase()) {
            case "post" -> postRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("POST_NOT_FOUND"));
            case "comment" -> commentRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("COMMENT_NOT_FOUND"));
            default -> throw new IllegalArgumentException("INVALID_REACTABLE_TYPE");
        };
    }
}
