package com.gautama.abscencerecordhitsbackend.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
public class RequestDTO {
    private LocalDate startedSkipping;
    private LocalDate finishedSkipping;
}
