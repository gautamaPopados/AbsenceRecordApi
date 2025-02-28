package com.gautama.abscencerecordhitsbackend.api.controller;

import com.gautama.abscencerecordhitsbackend.api.dto.*;
import com.gautama.abscencerecordhitsbackend.core.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import io.swagger.v3.oas.annotations.Parameter;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

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

    @DeleteMapping(value = "/file/unpin/{requestId}/{fileId}")
    public ResponseEntity<String> unpinConfirmationFile(@PathVariable Long requestId, @PathVariable Long fileId) {
        requestService.unpinFile(requestId, fileId);
        return ResponseEntity.status(HttpStatus.OK).body("Файл был успешно откреплен");
    }

    @GetMapping("/request_info/{id}")
    public ResponseEntity<RequestDetailsDTO> getRequestWithFileDownloadLink(@PathVariable Long id) throws AccessDeniedException {
        RequestDetailsDTO requestDetailsDTO = requestService.getRequestWithFileDownloadLink(id);
        return ResponseEntity.ok(requestDetailsDTO);
    }

    @GetMapping("/files/{fileId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long fileId) {
        return requestService.downloadFile(fileId);
    }
}
