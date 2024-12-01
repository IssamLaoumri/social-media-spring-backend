package com.laoumri.socialmediaspringbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MediaRequest {
    private String type;
    private String url;
}
