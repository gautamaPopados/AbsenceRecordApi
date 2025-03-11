package com.gautama.abscencerecordhitsbackend.api.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RequestListDTO {
    private Long id;
    private LocalDate startedSkipping;
    private LocalDate finishedSkipping;
    private String status;
    private UserDTO user;
}
