package com.laoumri.socialmediaspringbackend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class RegisterRequest {
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String gender;
    private int bDay;
    private int bMonth;
    private int bYear;
    private Set<String> roles;
}
