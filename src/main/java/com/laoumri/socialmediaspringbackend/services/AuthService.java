package com.laoumri.socialmediaspringbackend.services;

import com.laoumri.socialmediaspringbackend.dto.LoginRequest;
import com.laoumri.socialmediaspringbackend.dto.RegisterRequest;
import com.laoumri.socialmediaspringbackend.entities.User;
import graphql.GraphQLContext;

public interface AuthService {
    User register(RegisterRequest request);
    User login(LoginRequest request, GraphQLContext context);
}
