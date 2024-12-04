package com.laoumri.socialmediaspringbackend.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "comments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment extends Reactable{
    private String content;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Media media;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "posted_in")
    private Post post;
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment parent;
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Comment> replies;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "comment_by")
    private User commentBy;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Reaction> reactions;
    private Instant commentedAt;
}
