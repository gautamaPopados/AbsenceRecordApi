package com.gautama.abscencerecordhitsbackend.api.dto;

import com.gautama.abscencerecordhitsbackend.api.enums.Group;
import com.gautama.abscencerecordhitsbackend.api.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Group studentGroup;
    private Role userRole;
}