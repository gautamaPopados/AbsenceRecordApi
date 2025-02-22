package com.gautama.abscencerecordhitsbackend.core.model;

import com.gautama.abscencerecordhitsbackend.api.enums.RequestStatus;
import com.gautama.abscencerecordhitsbackend.core.converter.LocalDateAttributeConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name= "requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = LocalDateAttributeConverter.class)
    private LocalDate startedSkipping;

    @Convert(converter = LocalDateAttributeConverter.class)
    private LocalDate finishedSkipping;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;
}
