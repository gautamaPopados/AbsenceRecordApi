package com.gautama.abscencerecordhitsbackend.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ExtendDateRequestDTO {
    private LocalDate extendSkipping;

    public ExtendDateRequestDTO(LocalDate extendSkipping) {
        this.extendSkipping = extendSkipping;
    }
}
