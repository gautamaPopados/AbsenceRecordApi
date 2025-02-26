package com.gautama.abscencerecordhitsbackend.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
public class RequestResultDTO {
    private Long id;
    private LocalDate startedSkipping;
    private LocalDate finishedSkipping;
}
