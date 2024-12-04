package com.laoumri.socialmediaspringbackend.configs;

import com.laoumri.socialmediaspringbackend.entities.Role;
import com.laoumri.socialmediaspringbackend.enums.ERole;
import com.laoumri.socialmediaspringbackend.repositories.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Slf4j
public class RolesInitializer {
    @Bean
    public CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            Role c1 = new Role(1, ERole.ROLE_USER);
            Role c2 = new Role(2, ERole.ROLE_MODERATOR);
            Role c3 = new Role(3, ERole.ROLE_ADMIN);
            List<Role> roles = List.of(c1, c2, c3);
            roleRepository.saveAll(roles);

            log.info("{} roles have been initialized in the database.", roles.size());
        };
    }

}
