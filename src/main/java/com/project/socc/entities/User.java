package com.project.socc.entities;

import com.project.socc.enums.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Um usuário pode ter múltiplos perfis (ex: ser "professor" e "coordenador" ao mesmo tempo),
 * e um mesmo perfil pode estar associado a vários usuários (ex: vários usuários são "estudante").
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    @Size(min = 5, max = 20)
    @Column(nullable = false, unique = true)
    private String username;

    @Size(min = 2, max = 100)
    @Column(nullable = false)
    private String name;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @Size(min = 11, max = 11, message = "Phone must have 11 characters")
    @Column(nullable = false)
    private String phone;

    @Column(nullable = true)
    private Integer workload; // Só existe para usuários do tipo PROFESSOR

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @ManyToMany
    @JoinTable(
            name = "user_profile",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "profile_id")
    )
    private List<Profile> profiles = new ArrayList<>();
}
