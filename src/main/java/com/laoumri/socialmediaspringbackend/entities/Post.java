package com.laoumri.socialmediaspringbackend.entities;

import com.laoumri.socialmediaspringbackend.enums.EPost;
import com.laoumri.socialmediaspringbackend.enums.EPostVisibility;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "posts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "VARCHAR(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;
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
    private Instant publishedAt;
}
