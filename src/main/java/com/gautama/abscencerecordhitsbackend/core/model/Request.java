package com.gautama.abscencerecordhitsbackend.core.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.gautama.abscencerecordhitsbackend.api.enums.RequestStatus;
import com.gautama.abscencerecordhitsbackend.core.converter.LocalDateAttributeConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<FileEntity> proofs;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private User user;
}
