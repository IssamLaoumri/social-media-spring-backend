package com.laoumri.socialmediaspringbackend.entities;

import com.laoumri.socialmediaspringbackend.enums.EMedia;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "media")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Media {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "VARCHAR(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;
    @Enumerated(EnumType.STRING)
    private EMedia type;
    private String url;

}
