package com.project.socc.entities;


import com.project.socc.enums.ProfileRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Um perfil pode ter várias permissões (ex: um "professor" pode manifestar interesse em um núcleo e ministrar turmas),
 * e uma mesma permissão pode ser compartilhada por vários perfis (ex: tanto "professor" quanto "coordenador" podem acessar o diário de classe).
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private ProfileRole role;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private boolean protectedProfile;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "profile_permission",
            joinColumns = @JoinColumn(name = "profile_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_code")
    )
    private List<Permission> permissions = new ArrayList<>();

    public void addPermission(Permission permission) {
        this.permissions.add(permission);
    }

    public void removePermission(Permission permission) {
        this.permissions.remove(permission);
    }
}
