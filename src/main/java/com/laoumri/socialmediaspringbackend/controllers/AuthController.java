package com.laoumri.socialmediaspringbackend.controllers;

import com.laoumri.socialmediaspringbackend.dto.requests.LoginRequest;
import com.laoumri.socialmediaspringbackend.dto.requests.RegisterRequest;
import com.laoumri.socialmediaspringbackend.entities.User;
import com.laoumri.socialmediaspringbackend.services.AuthService;
import graphql.GraphQLContext;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @MutationMapping(name = "register")
    public User register(@Argument(name = "input") RegisterRequest request){
     return authService.register(request);
    }

    @MutationMapping(name = "login")
    public User login(@Argument(name = "input") LoginRequest request, GraphQLContext context){
     return authService.login(request, context);
    }
}