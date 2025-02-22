package com.gautama.abscencerecordhitsbackend.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RequestDTO {
    private LocalDate startedSkipping;
    private LocalDate finishedSkipping;

    public RequestDTO(LocalDate startedSkipping, LocalDate finishedSkipping) {
        this.startedSkipping = startedSkipping;
        this.finishedSkipping = finishedSkipping;
    }
}
