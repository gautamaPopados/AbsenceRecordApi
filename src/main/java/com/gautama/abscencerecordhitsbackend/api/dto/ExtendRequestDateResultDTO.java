package com.gautama.abscencerecordhitsbackend.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ExtendRequestDateResultDTO {
    private Long id;
    private LocalDate finishedExtendSkipping;

    public ExtendRequestDateResultDTO(Long id, LocalDate finishedExtendSkipping) {
        this.id = id;
        this.finishedExtendSkipping = finishedExtendSkipping;
    }
}
