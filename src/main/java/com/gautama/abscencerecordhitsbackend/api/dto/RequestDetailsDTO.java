package com.gautama.abscencerecordhitsbackend.api.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class RequestDetailsDTO {
    private Long id;
    private LocalDate startedSkipping;
    private LocalDate finishedSkipping;
    private String status;
    private List<FileInfoDto> files;
    private UserDTO user;
}
