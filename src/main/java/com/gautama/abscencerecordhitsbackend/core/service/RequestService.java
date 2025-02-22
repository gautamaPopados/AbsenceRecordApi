package com.gautama.abscencerecordhitsbackend.core.service;

import com.gautama.abscencerecordhitsbackend.api.dto.RequestDTO;
import com.gautama.abscencerecordhitsbackend.api.mapper.RequestMapper;
import com.gautama.abscencerecordhitsbackend.core.model.Request;
import com.gautama.abscencerecordhitsbackend.core.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
public class RequestService {
    public final RequestRepository requestRepository;
    private final RequestMapper requestMapper;

    public RequestService(RequestRepository requestRepository, RequestMapper requestMapper) {
        this.requestRepository = requestRepository;
        this.requestMapper = requestMapper;
    }

    public Request saveRequest(Request request) {
        return requestRepository.save(request);
    }

    public RequestDTO addRequest(RequestDTO requestDTO) {
        Request newRequest = requestMapper.toEntity(requestDTO);
        Request savedRequest = saveRequest(newRequest);
        return requestMapper.toDto(savedRequest);
    }
}
