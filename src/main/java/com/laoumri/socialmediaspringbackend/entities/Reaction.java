package com.laoumri.socialmediaspringbackend.entities;

import com.laoumri.socialmediaspringbackend.enums.EReact;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "reactions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Reaction {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "VARCHAR(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;
    @Enumerated(EnumType.STRING)
    private EReact react;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Reactable reactable;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private User reactedBy;
}
