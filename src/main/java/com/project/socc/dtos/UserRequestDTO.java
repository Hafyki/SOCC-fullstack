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

@Getter
@Setter
@NoArgsConstructor
public class UserRequestDTO {

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

    public UserRequestDTO(User user) {
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
                .phone(this.phone)
                .status(UserStatus.SUSPENDED) // Valor padrão (depois que atribui o perfil passa para ativo)
                .profiles(new ArrayList<>()) // Cria a lista de perfis vazia
                .build();
    }
}
