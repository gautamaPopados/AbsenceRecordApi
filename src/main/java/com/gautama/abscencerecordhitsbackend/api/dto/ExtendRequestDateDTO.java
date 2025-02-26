package com.gautama.abscencerecordhitsbackend.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ExtendRequestDateDTO {
    private LocalDate extendSkipping;

    public ExtendRequestDateDTO(LocalDate extendSkipping) {
        this.extendSkipping = extendSkipping;
    }
}
