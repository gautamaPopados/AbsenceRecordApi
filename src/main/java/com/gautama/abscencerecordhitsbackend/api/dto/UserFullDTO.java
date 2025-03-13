package com.gautama.abscencerecordhitsbackend.api.dto;

import com.gautama.abscencerecordhitsbackend.api.enums.Group;
import com.gautama.abscencerecordhitsbackend.api.enums.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserFullDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Group studentGroup;
    private Role userRole;
    private List<RequestResultDTO> requestList;
}