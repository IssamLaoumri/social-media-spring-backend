package com.laoumri.socialmediaspringbackend.entities;

import com.laoumri.socialmediaspringbackend.enums.EPost;
import com.laoumri.socialmediaspringbackend.enums.EPostVisibility;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "posts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post extends Reactable{
    @Enumerated(EnumType.STRING)
    private EPost type;
    @Enumerated(EnumType.STRING)
    private EPostVisibility visibility;
    private String content;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Media> media;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post", fetch = FetchType.LAZY)
    private List<Comment> comments;
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "reactable")
    private Set<Reaction> reactions;
    private Instant publishedAt;
}
