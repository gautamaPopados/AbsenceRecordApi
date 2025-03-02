package com.gautama.abscencerecordhitsbackend.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Normalized;

@Getter
@Setter
@AllArgsConstructor
public class LoginDTO {
    @NotNull
    String email;
    @NotNull
    String password;
}
