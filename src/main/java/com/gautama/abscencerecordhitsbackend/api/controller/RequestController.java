package com.gautama.abscencerecordhitsbackend.api.controller;

import com.gautama.abscencerecordhitsbackend.api.dto.ExtendDateRequestDTO;
import com.gautama.abscencerecordhitsbackend.api.dto.RequestDTO;
import com.gautama.abscencerecordhitsbackend.api.dto.ResultExtendRequestDate;
import com.gautama.abscencerecordhitsbackend.core.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;

    @PostMapping("/request")
    public ResponseEntity<RequestDTO> addNewRequest(@RequestBody RequestDTO requestDTO) {
        RequestDTO savedRequest = requestService.addRequest(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRequest);
    }

    @PatchMapping("/request/{id}")
    public ResponseEntity<?> extendDateRequest(@PathVariable Long id, @RequestBody ExtendDateRequestDTO extendDateDTO) {
        ResultExtendRequestDate extendRequest = requestService.extendDate(id, extendDateDTO);
        if (extendRequest != null) {
            return ResponseEntity.status(HttpStatus.OK).body(extendRequest);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Неккоректные входные данные для даты");
    }
}
