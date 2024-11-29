package com.laoumri.socialmediaspringbackend.services;

import com.laoumri.socialmediaspringbackend.dto.requests.LoginRequest;
import com.laoumri.socialmediaspringbackend.dto.requests.RegisterRequest;
import com.laoumri.socialmediaspringbackend.entities.User;
import graphql.GraphQLContext;

public interface AuthService {
    User register(RegisterRequest request);
    User login(LoginRequest request, GraphQLContext context);
}
