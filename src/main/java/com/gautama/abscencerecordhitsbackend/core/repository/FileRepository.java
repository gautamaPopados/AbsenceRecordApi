package com.gautama.abscencerecordhitsbackend.core.repository;

import com.gautama.abscencerecordhitsbackend.core.model.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {
    Optional<FileEntity> findByFileName(String fileName);
}

