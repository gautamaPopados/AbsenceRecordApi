package com.gautama.abscencerecordhitsbackend.core.repository;

import com.gautama.abscencerecordhitsbackend.api.enums.UserRole;
import com.gautama.abscencerecordhitsbackend.core.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findAllByUserRole(UserRole role);
    List<User> findAllByUserRoleIsNull();
}