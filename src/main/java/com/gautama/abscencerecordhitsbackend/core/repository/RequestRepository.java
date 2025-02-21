package com.gautama.abscencerecordhitsbackend.core.repository;

import com.gautama.abscencerecordhitsbackend.core.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Long> {
}
