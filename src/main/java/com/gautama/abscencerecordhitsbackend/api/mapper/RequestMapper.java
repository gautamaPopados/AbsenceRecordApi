package com.gautama.abscencerecordhitsbackend.api.mapper;

import com.gautama.abscencerecordhitsbackend.api.dto.RequestDTO;
import com.gautama.abscencerecordhitsbackend.api.dto.ResultExtendRequestDate;
import com.gautama.abscencerecordhitsbackend.api.enums.RequestStatus;
import com.gautama.abscencerecordhitsbackend.core.model.Request;
import org.springframework.stereotype.Component;

@Component
public class RequestMapper {
    public RequestDTO toDto(Request request) {
        if (request == null) {
            return null;
        }
        return new RequestDTO(
                request.getStartedSkipping(),
                request.getFinishedSkipping()
        );
    }

    public Request toEntity(RequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }
        return Request.builder()
                .startedSkipping(requestDTO.getStartedSkipping())
                .finishedSkipping(requestDTO.getFinishedSkipping())
                .status(RequestStatus.PENDING)
                .build();
    }

    public ResultExtendRequestDate toResultChange(Request request) {
        if (request == null) {
            return null;
        }

        return new ResultExtendRequestDate(
                request.getId(),
                request.getFinishedSkipping()
        );
    }
}
