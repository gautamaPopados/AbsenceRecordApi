package com.gautama.abscencerecordhitsbackend.api.dto;

import lombok.Data;

@Data
public class FileInfoDto {
    private Long id;
    private String fileName;
    private String downloadUrl;
}