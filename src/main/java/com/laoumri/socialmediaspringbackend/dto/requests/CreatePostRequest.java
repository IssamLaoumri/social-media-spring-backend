package com.laoumri.socialmediaspringbackend.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CreatePostRequest {
    private String visibility;
    private String type;
    private String content;
    private List<MediaRequest> media;
}
