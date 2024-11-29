package com.laoumri.socialmediaspringbackend.entities;

import com.laoumri.socialmediaspringbackend.enums.ERole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "roles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Role implements GrantedAuthority {
    @Id
    private int id;
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;

    @Override
    public String getAuthority() {
        return name.name();
    }
}
