package com.gautama.abscencerecordhitsbackend.core.service;

import com.gautama.abscencerecordhitsbackend.api.dto.ExtendRequestDateDTO;
import com.gautama.abscencerecordhitsbackend.api.dto.RequestDTO;
import com.gautama.abscencerecordhitsbackend.api.dto.ExtendRequestDateResultDTO;
import com.gautama.abscencerecordhitsbackend.api.dto.RequestResultDTO;
import com.gautama.abscencerecordhitsbackend.api.enums.RequestStatus;
import com.gautama.abscencerecordhitsbackend.api.mapper.RequestMapper;
import com.gautama.abscencerecordhitsbackend.core.model.Request;
import com.gautama.abscencerecordhitsbackend.core.repository.RequestRepository;
import com.gautama.abscencerecordhitsbackend.core.validator.DateValidator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


@Service
public class RequestService {
    public final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final DateValidator dateValidator;

    public RequestService(RequestRepository requestRepository, RequestMapper requestMapper, DateValidator dateValidator) {
        this.requestRepository = requestRepository;
        this.requestMapper = requestMapper;
        this.dateValidator = dateValidator;
    }


    public RequestResultDTO createRequest(RequestDTO requestDTO) {
        if (dateValidator.checkDate(requestDTO.getStartedSkipping(), requestDTO.getFinishedSkipping())) {
            Request newRequest = requestMapper.toEntity(requestDTO);
            Request savedRequest = requestRepository.save(newRequest);
            return requestMapper.toDto(savedRequest);
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Некорректные входные данные для даты.");
    }

    public void changeRequestStatus(Long id, RequestStatus requestStatus) {
        Request request = requestRepository.findById(id).orElseThrow(() -> new RuntimeException("Заявка не найдена."));
        request.setStatus(requestStatus);
        requestRepository.save(request);
    }

    @Transactional
    public ExtendRequestDateResultDTO extendDate(Long id, ExtendRequestDateDTO extendDateDTO) {
        return requestRepository.findById(id)
                .filter(request -> dateValidator.checkDate(request.getFinishedSkipping(), extendDateDTO.getExtendSkipping()))
                .map(request -> {
                    request.setFinishedSkipping(extendDateDTO.getExtendSkipping());
                    return requestRepository.save(request);
                })
                .map(requestMapper::toResultChange)
                .orElse(null);
    }
}
