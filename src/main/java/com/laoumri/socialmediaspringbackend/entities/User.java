package com.laoumri.socialmediaspringbackend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.laoumri.socialmediaspringbackend.enums.EGender;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class User implements UserDetails {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstname;
    private String lastname;
    private String _username;
    private String email;
    @JsonIgnore
    private String password;
    private int bDay;
    private int bMonth;
    private int bYear;
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EGender gender;
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    // USER FUNCTIONALITY DATA
    private String profilePictureUrl;
    private String profileCoverUrl;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("publishedAt desc")
    private List<Post> posts;

    // META-DATA
    private boolean accountLocked = false;
    private boolean accountExpired = false;
    private boolean credentialsExpired = false;
    private boolean enabled = false;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !accountExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }

    @Override
    public boolean isEnabled() {
        return !enabled;
    }
}
