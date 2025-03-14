package com.gautama.abscencerecordhitsbackend.api.mapper;

import com.gautama.abscencerecordhitsbackend.api.dto.*;
import com.gautama.abscencerecordhitsbackend.api.enums.RequestStatus;
import com.gautama.abscencerecordhitsbackend.core.model.Request;
import com.gautama.abscencerecordhitsbackend.core.model.User;
import org.springframework.stereotype.Component;

@Component
public class RequestMapper {

    public RequestResultDTO toDto(Request request) {
        if (request == null) {
            return null;
        }
        return new RequestResultDTO(
                request.getId(),
                request.getStartedSkipping(),
                request.getFinishedSkipping()
        );
    }

    public Request toEntity(RequestDTO requestDTO, User user) {
        if (requestDTO == null) {
            return null;
        }
        return Request.builder()
                .startedSkipping(requestDTO.getStartedSkipping())
                .finishedSkipping(requestDTO.getFinishedSkipping())
                .user(user)
                .status(RequestStatus.PENDING)
                .build();
    }

    public ExtendRequestDateResultDTO toResultChange(Request request) {
        if (request == null) {
            return null;
        }

        return new ExtendRequestDateResultDTO(
                request.getId(),
                request.getFinishedSkipping()
        );
    }


    public RequestListDTO requestToRequestListDTO(Request request) {
        if (request == null) {
            return null;
        }

        RequestListDTO dto = new RequestListDTO();
        dto.setId(request.getId());
        dto.setStartedSkipping(request.getStartedSkipping());
        dto.setFinishedSkipping(request.getFinishedSkipping());
        dto.setStatus(String.valueOf(request.getStatus()));

        User user = request.getUser();
        if (user != null) {
            UserDTO userDTO = new UserDTO(
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getStudentGroup(),
                    user.getRole());
            dto.setUser(userDTO);
        }

        return dto;
    }
}
