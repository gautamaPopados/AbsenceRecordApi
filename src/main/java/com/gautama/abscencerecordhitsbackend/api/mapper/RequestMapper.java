package com.gautama.abscencerecordhitsbackend.api.mapper;

import com.gautama.abscencerecordhitsbackend.api.dto.RequestDTO;
import com.gautama.abscencerecordhitsbackend.api.dto.ExtendRequestDateResultDTO;
import com.gautama.abscencerecordhitsbackend.api.dto.RequestResultDTO;
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
}
