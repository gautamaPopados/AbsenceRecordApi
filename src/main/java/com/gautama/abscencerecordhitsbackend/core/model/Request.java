package com.gautama.abscencerecordhitsbackend.core.model;

import com.gautama.abscencerecordhitsbackend.api.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate startedSkipping;
    private LocalDate finishedSkipping;
    @Enumerated(EnumType.STRING)
    private RequestStatus status;
}
