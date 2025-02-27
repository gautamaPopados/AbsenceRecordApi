package com.gautama.abscencerecordhitsbackend.api.controller;

import com.gautama.abscencerecordhitsbackend.api.dto.ExtendRequestDateDTO;
import com.gautama.abscencerecordhitsbackend.api.dto.FileResultDTO;
import com.gautama.abscencerecordhitsbackend.api.dto.RequestDTO;
import com.gautama.abscencerecordhitsbackend.api.dto.ExtendRequestDateResultDTO;
import com.gautama.abscencerecordhitsbackend.api.dto.RequestResultDTO;
import com.gautama.abscencerecordhitsbackend.core.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import io.swagger.v3.oas.annotations.Parameter;

import java.io.IOException;


@RestController
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;

    @PostMapping("/request")
    public ResponseEntity<RequestResultDTO> createRequest(@RequestBody RequestDTO requestDTO) {
        RequestResultDTO savedRequestDto = requestService.createRequest(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRequestDto);
    }

    @PatchMapping("/request/{id}")
    public ResponseEntity<?> extendDateRequest(@PathVariable Long id, @RequestBody ExtendRequestDateDTO extendDateDTO) {
        ExtendRequestDateResultDTO extendRequest = requestService.extendDate(id, extendDateDTO);
        if (extendRequest != null) {
            return ResponseEntity.status(HttpStatus.OK).body(extendRequest);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Неккоректные входные данные для даты");
    }

    @PostMapping(value = "/upload/{requestId}", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadFile(
            @PathVariable Long requestId,
            @Parameter(description = "Файл для загрузки")
            @RequestParam("file") MultipartFile file) {
        try {
            FileResultDTO fileResultDTO = requestService.addFile(requestId, file);
            return ResponseEntity.status(HttpStatus.OK).body(fileResultDTO);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при прикреплении файла: " + e.getMessage());
        }
    }
}
