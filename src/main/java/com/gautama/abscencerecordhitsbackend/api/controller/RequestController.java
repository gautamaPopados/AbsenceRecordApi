package com.gautama.abscencerecordhitsbackend.api.controller;

import com.gautama.abscencerecordhitsbackend.api.dto.RequestDTO;
import com.gautama.abscencerecordhitsbackend.core.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;

    @PostMapping("/requests")
    public ResponseEntity<RequestDTO> addNewRequest(@RequestBody RequestDTO requestDTO) {
        RequestDTO savedRequest = requestService.addRequest(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRequest);
    }
}
