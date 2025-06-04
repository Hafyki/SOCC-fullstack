package com.project.socc.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateRequestDTO {

    @Size(min = 5, max = 20, message = "Username must be between 5 and 20 characters")
    private String username;

    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @Email(message = "Email should be valid")
    private String email;

    @Size(min = 11, max = 11, message = "Phone must have 11 characters")
    private String phone;

    private Integer workload;
    private String status;
    private List<Long> profileIds;
}
