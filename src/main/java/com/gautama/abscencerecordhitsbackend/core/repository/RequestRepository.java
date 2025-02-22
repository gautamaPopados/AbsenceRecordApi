package com.gautama.abscencerecordhitsbackend.core.repository;

import com.gautama.abscencerecordhitsbackend.core.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
}