package com.gautama.abscencerecordhitsbackend.core.service;

import com.gautama.abscencerecordhitsbackend.api.dto.ExtendDateRequestDTO;
import com.gautama.abscencerecordhitsbackend.api.dto.RequestDTO;
import com.gautama.abscencerecordhitsbackend.api.dto.ResultExtendRequestDate;
import com.gautama.abscencerecordhitsbackend.api.mapper.RequestMapper;
import com.gautama.abscencerecordhitsbackend.core.model.Request;
import com.gautama.abscencerecordhitsbackend.core.repository.RequestRepository;
import com.gautama.abscencerecordhitsbackend.core.validator.DateValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.util.Optional;


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

    public Request saveRequest(Request request) {
        return requestRepository.save(request);
    }

    public RequestDTO addRequest(RequestDTO requestDTO) {
        //проверка авторизации у пользователя
        Request newRequest = requestMapper.toEntity(requestDTO);
        Request savedRequest = saveRequest(newRequest);
        return requestMapper.toDto(savedRequest);
    }

    @Transactional
    public ResultExtendRequestDate extendDate(Long id, ExtendDateRequestDTO extendDateDTO) {
        //проверка авторизации у пользователя
        return requestRepository.findById(id)
                .filter(request -> dateValidator.checkDate(request.getFinishedSkipping(), extendDateDTO.getExtendSkipping()))
                .map(request -> {
                    request.setFinishedSkipping(extendDateDTO.getExtendSkipping());
                    return saveRequest(request);
                })
                .map(requestMapper::toResultChange)
                .orElse(null);
    }
}
