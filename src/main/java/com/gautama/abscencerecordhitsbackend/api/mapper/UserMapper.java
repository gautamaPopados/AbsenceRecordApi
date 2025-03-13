package com.gautama.abscencerecordhitsbackend.api.mapper;

import com.gautama.abscencerecordhitsbackend.api.dto.UserDTO;
import com.gautama.abscencerecordhitsbackend.api.dto.UserFullDTO;
import com.gautama.abscencerecordhitsbackend.core.model.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {

    private final RequestMapper requestMapper;

    public UserMapper(RequestMapper requestMapper) {
        this.requestMapper = requestMapper;
    }

    public UserDTO userToUserDto(User user) {
        return new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getStudentGroup(),
                user.getRole()
        );
    }
    public UserFullDTO userToUserFullDto(User user) {
        UserFullDTO dto = new UserFullDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setUserRole(user.getRole());
        dto.setStudentGroup(user.getStudentGroup());
        dto.setRequestList(user.getRequests().stream()
                .map(requestMapper::toDto)
                .collect(Collectors.toList()));
        return dto;
    }
}