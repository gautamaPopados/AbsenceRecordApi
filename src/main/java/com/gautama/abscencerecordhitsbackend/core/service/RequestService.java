package com.gautama.abscencerecordhitsbackend.core.service;

import com.gautama.abscencerecordhitsbackend.api.dto.ExtendRequestDateDTO;
import com.gautama.abscencerecordhitsbackend.api.dto.RequestDTO;
import com.gautama.abscencerecordhitsbackend.api.dto.ExtendRequestDateResultDTO;
import com.gautama.abscencerecordhitsbackend.api.dto.RequestResultDTO;
import com.gautama.abscencerecordhitsbackend.api.enums.RequestStatus;
import com.gautama.abscencerecordhitsbackend.api.dto.*;
import com.gautama.abscencerecordhitsbackend.api.mapper.RequestMapper;
import com.gautama.abscencerecordhitsbackend.core.model.FileEntity;
import com.gautama.abscencerecordhitsbackend.core.model.Request;
import com.gautama.abscencerecordhitsbackend.core.model.User;
import com.gautama.abscencerecordhitsbackend.core.repository.FileRepository;
import com.gautama.abscencerecordhitsbackend.core.repository.RequestRepository;
import com.gautama.abscencerecordhitsbackend.core.repository.UserRepository;
import com.gautama.abscencerecordhitsbackend.core.validator.DateValidator;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class RequestService {
    public final RequestRepository requestRepository;
    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final RequestMapper requestMapper;
    private final DateValidator dateValidator;

    public RequestService(RequestRepository requestRepository, FileRepository fileRepository, UserRepository userRepository, UserService userService,
                          RequestMapper requestMapper, DateValidator dateValidator) {
        this.requestRepository = requestRepository;
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.requestMapper = requestMapper;
        this.dateValidator = dateValidator;
    }

    public Request getRequest(Long id) {
        return requestRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Заявки с указанным Id: " + id + " не существует"));
    }

    public Request saveRequest(Request request) {
        return requestRepository.save(request);
    }


    public void saveFile(FileEntity fileEntity) {
        fileRepository.save(fileEntity);
    }

    public RequestResultDTO createRequest(RequestDTO requestDTO) {
        if (dateValidator.checkDate(requestDTO.getStartedSkipping(), requestDTO.getFinishedSkipping())) {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = userService.loadUserByUsername(userDetails.getUsername());

            Request newRequest = requestMapper.toEntity(requestDTO, user);
            Request savedRequest = saveRequest(newRequest);
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
                    return saveRequest(request);
                })
                .map(requestMapper::toResultChange)
                .orElse(null);
    }

    public FileResultDTO addFile(Long id, MultipartFile file) throws IOException {
        Request request = getRequest(id);

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userService.loadUserByUsername(userDetails.getUsername());

        if (!request.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Вы не можете прикреплять файлы к чужой заявке!");
        }

        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileName(file.getOriginalFilename());
        fileEntity.setFileData(file.getBytes());
        fileEntity.setRequest(request);

        if (request.getProofs() != null) {
            request.getProofs().add(fileEntity);
        } else {
            List<FileEntity> proofs = new ArrayList<>();
            proofs.add(fileEntity);
            request.setProofs(proofs);
        }
        saveFile(fileEntity);
        saveRequest(request);

        FileResultDTO fileResultDTO = new FileResultDTO();
        fileResultDTO.setFileName(fileEntity.getFileName());
        fileResultDTO.setId(fileEntity.getId());

        return fileResultDTO;
    }

    @Transactional
    public void unpinFile(Long requestId, Long fileId) {
        Request request = getRequest(requestId);

        FileEntity fileToRemove = request.getProofs().stream()
                .filter(file -> file.getId().equals(fileId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Файл с id " + fileId + " не найден в заявке с id " + requestId));

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userService.loadUserByUsername(userDetails.getUsername());

        if (!request.getUser().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Это не ваша заявки либо не ваш файл");
        }

        request.getProofs().remove(fileToRemove);

        requestRepository.save(request);
    }

    @Transactional(readOnly = true)
    public RequestDetailsDTO getRequestWithFileDownloadLink(Long requestId) throws AccessDeniedException {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Заявки с указанным id не найдено: " + requestId));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с email " + email + " не найден"));
        Long userId = currentUser.getId();

        boolean isStudent = authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("STUDENT"));

        if (isStudent && !request.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("Вы не можете просматривать чужие заявки");
        }

        RequestDetailsDTO requestDetailsDTO = new RequestDetailsDTO();
        requestDetailsDTO.setId(request.getId());
        requestDetailsDTO.setStartedSkipping(request.getStartedSkipping());
        requestDetailsDTO.setFinishedSkipping(request.getFinishedSkipping());
        requestDetailsDTO.setStatus(request.getStatus().toString());
        requestDetailsDTO.setUserId(request.getUser().getId());

        List<FileInfoDto> fileInfoDtos = request.getProofs().stream()
                .map(fileEntity -> {
                    FileInfoDto fileInfoDto = new FileInfoDto();
                    fileInfoDto.setId(fileEntity.getId());
                    fileInfoDto.setFileName(fileEntity.getFileName());
                    String downloadURL = ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path("/api/requests/files/")
                            .path(String.valueOf(fileEntity.getId()))
                            .toUriString();
                    fileInfoDto.setDownloadUrl(downloadURL);
                    return fileInfoDto;
                })
                .collect(Collectors.toList());

        requestDetailsDTO.setFiles(fileInfoDtos);

        return requestDetailsDTO;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<byte[]> downloadFile(Long fileId) {
        FileEntity fileEntity = fileRepository.findById(fileId)
                .orElseThrow(() -> new EntityNotFoundException("Файл с указанным id не найден: " + fileId));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileEntity.getFileName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fileEntity.getFileData());
    }

    public List<RequestListDTO> getAllRequests(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isStudent = authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("STUDENT"));

        List<Request> requests;

        if (isStudent) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();

            User currentUser = userRepository.findByEmail(email)
                    .orElseThrow(() -> new EntityNotFoundException("Пользователь с email " + email + " не найден"));
            Long currentUserId = currentUser.getId();
            requests = requestRepository.findByUser_Id(currentUserId);
        } else {
            if (userId != null) {
                requests = requestRepository.findByUser_Id(userId);
            } else {
                requests = requestRepository.findAll();
            }
        }

        return requests.stream()
                .map(requestMapper::requestToRequestListDTO)
                .collect(Collectors.toList());
    }
}
