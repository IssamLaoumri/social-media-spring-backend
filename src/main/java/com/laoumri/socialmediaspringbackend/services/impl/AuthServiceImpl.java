package com.laoumri.socialmediaspringbackend.services.impl;

import com.laoumri.socialmediaspringbackend.dto.LoginRequest;
import com.laoumri.socialmediaspringbackend.dto.RegisterRequest;
import com.laoumri.socialmediaspringbackend.entities.Role;
import com.laoumri.socialmediaspringbackend.entities.User;
import com.laoumri.socialmediaspringbackend.enums.EGender;
import com.laoumri.socialmediaspringbackend.enums.ERole;
import com.laoumri.socialmediaspringbackend.exceptions.EmailAddressAlreadyExists;
import com.laoumri.socialmediaspringbackend.exceptions.RoleNotFoundException;
import com.laoumri.socialmediaspringbackend.repositories.RoleRepository;
import com.laoumri.socialmediaspringbackend.repositories.UserRepository;
import com.laoumri.socialmediaspringbackend.security.services.JwtUtils;
import com.laoumri.socialmediaspringbackend.services.AuthService;
import graphql.GraphQLContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    @Value("${jwt.cookieName}")
    private String cookieName;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtUtils jwtUtils;

    @Override
    public User register(RegisterRequest request) {
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAddressAlreadyExists();
        }

        Set<Role> roles = new HashSet<>();
        if(request.getRoles() == null) {
            Role role = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(()-> new RoleNotFoundException(ERole.ROLE_USER.name()));
            roles.add(role);
        } else  {
            request.getRoles().forEach(role -> {
                try {
                    Role roleEntity = roleRepository.findByName(ERole.valueOf(role))
                            .orElseThrow(()-> new RoleNotFoundException(role));
                    roles.add(roleEntity);
                }catch (IllegalArgumentException e){
                    throw new RoleNotFoundException(role);
                }
            });
        }

        User user = User.builder()
                .email(request.getEmail())
                ._username(generateUniqueUsername(request.getFirstname(), request.getLastname()))
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .password(passwordEncoder.encode(request.getPassword()))
                .gender(EGender.valueOf(request.getGender()))
                .bDay(request.getBDay())
                .bMonth(request.getBMonth())
                .bYear(request.getBYear())
                .roles(roles)
                .build();
        return userRepository.save(user);
    }

    @Override
    public User login(LoginRequest request, GraphQLContext context) throws AuthenticationException {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String jwtToken = jwtUtils.generateJwtTokenFromEmail(userDetails.getUsername());
        context.put(cookieName, jwtToken);

        return (User) authentication.getPrincipal();
    }

    private String generateUniqueUsername(String firstname, String lastname) {
        boolean a;
        String username = (firstname+"."+lastname).toLowerCase();
        do{
            boolean check = userRepository.existsBy_username(username);
            if(check){
                username += String.valueOf(System.currentTimeMillis() * Math.random()).substring(0, 1);
                a = true;
            } else {
                a = false;
            }
        }while(a);
        return username;
    }
}
