package com.project.socc.dtos;

import com.project.socc.entities.User;
import com.project.socc.enums.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

/**
 * De acordo com as regras de negócio da documentação do nosso caso de uso, somente o diretor e o vice-diretor
 * podem: definir a carga horária mínima de um docente, alterar o status de um usuário e atribuir perfis ao usuário.
 * Logo, no POST é criado, na verdade, um 'pré-cadastro' no sistema com as informações pessoais do usuário.
 * Os campos [workload, status e profiles] serão setados/modificados posteriormente pelo diretor ou vice-diretor através de um PATCH.
 */

@Getter
@Setter
@NoArgsConstructor
public class UserCreateRequestDTO {

    @NotBlank
    @Size(min = 5, max = 20, message = "Username must be between 5 and 20 characters")
    private String username;

    @NotBlank
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank
    @Size(min = 11, max = 11, message = "Phone must have 11 characters")
    private String phone;

    public UserCreateRequestDTO(User user) {
        this.username = user.getUsername();
        this.name = user.getName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
    }

    public User toEntity() {
        return User.builder()
                .username(this.username)
                .name(this.name)
                .email(this.email)
                .phone(this.phone) // O workload é setado como null inicialmente (até porque só usuários com perfil de professor terão esse valor setado, para os outros perfis é null)
                .status(UserStatus.SUSPENDED) // Valor padrão (depois que atribui algum perfil ao usuário, passa para ativo)
                .profiles(new ArrayList<>()) // Cria a lista de perfis vazia (será setada posteriormente pelo diretor ou vice-diretor)
                .build();
    }
}
