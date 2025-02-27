package com.gautama.abscencerecordhitsbackend.core.service;

import com.gautama.abscencerecordhitsbackend.api.dto.ExtendRequestDateDTO;
import com.gautama.abscencerecordhitsbackend.api.dto.FileResultDTO;
import com.gautama.abscencerecordhitsbackend.api.dto.RequestDTO;
import com.gautama.abscencerecordhitsbackend.api.dto.ExtendRequestDateResultDTO;
import com.gautama.abscencerecordhitsbackend.api.dto.RequestResultDTO;
import com.gautama.abscencerecordhitsbackend.api.mapper.RequestMapper;
import com.gautama.abscencerecordhitsbackend.core.model.FileEntity;
import com.gautama.abscencerecordhitsbackend.core.model.Request;
import com.gautama.abscencerecordhitsbackend.core.model.User;
import com.gautama.abscencerecordhitsbackend.core.repository.FileRepository;
import com.gautama.abscencerecordhitsbackend.core.repository.RequestRepository;
import com.gautama.abscencerecordhitsbackend.core.repository.UserRepository;
import com.gautama.abscencerecordhitsbackend.core.validator.DateValidator;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;


@Service
public class RequestService {
    public final RequestRepository requestRepository;
    private final FileRepository fileRepository;
    private final UserService userService;
    private final RequestMapper requestMapper;
    private final DateValidator dateValidator;

    public RequestService(RequestRepository requestRepository, FileRepository fileRepository, UserService userService,
                          RequestMapper requestMapper, DateValidator dateValidator) {
        this.requestRepository = requestRepository;
        this.fileRepository = fileRepository;
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
}
