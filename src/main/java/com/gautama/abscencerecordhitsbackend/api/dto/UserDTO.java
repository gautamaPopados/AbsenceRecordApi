package com.gautama.abscencerecordhitsbackend.api.dto;

import com.gautama.abscencerecordhitsbackend.api.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private UserRole userRole;
}