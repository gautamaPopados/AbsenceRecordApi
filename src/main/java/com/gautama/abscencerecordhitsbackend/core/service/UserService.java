package com.gautama.abscencerecordhitsbackend.core.service;

import com.gautama.abscencerecordhitsbackend.api.enums.UserRole;
import com.gautama.abscencerecordhitsbackend.core.model.User;
import com.gautama.abscencerecordhitsbackend.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User grantDeanRole(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь с ID " + userId + " не найден.");
        }

        User user = optionalUser.get();
        if (user.getUserRole() == UserRole.DEANERY) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "У пользователя с ID " + userId + " уже есть роль Деканат.");
        }

        user.setUserRole(UserRole.DEANERY);
        return userRepository.save(user);
    }

    public User grantRole(Long userId, UserRole role) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь с ID " + userId + " не найден.");
        }

        User user = optionalUser.get();

        if (role == UserRole.DEANERY) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Назначение роли Деканат доступно только Админу.");
        }

        if (user.getUserRole() == role) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "У пользователя с ID " + userId + " уже есть роль " + role.getDisplayName() + ".");
        }

        user.setUserRole(role);
        return userRepository.save(user);
    }

}