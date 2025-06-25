package com.project.socc.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission {

    @Id
    private String code; // Código único da permissão (inserido manualmente)

    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = false)
    private String description;
}
