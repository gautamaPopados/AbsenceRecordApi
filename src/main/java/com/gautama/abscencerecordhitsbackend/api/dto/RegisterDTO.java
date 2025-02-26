package com.gautama.abscencerecordhitsbackend.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RegisterDTO {
    String firstName;
    String lastName;
    String email;
    String password;
}
