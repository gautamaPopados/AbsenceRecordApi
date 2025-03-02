package com.gautama.abscencerecordhitsbackend.core.service;

import com.gautama.abscencerecordhitsbackend.api.dto.UserDTO;
import com.gautama.abscencerecordhitsbackend.api.enums.UserQueryType;
import com.gautama.abscencerecordhitsbackend.api.enums.Role;
import com.gautama.abscencerecordhitsbackend.api.mapper.UserMapper;
import com.gautama.abscencerecordhitsbackend.core.model.User;
import com.gautama.abscencerecordhitsbackend.core.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public User grantDeanRole(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new NoSuchElementException("Пользователь с ID " + userId + " не найден.");
        }

        User user = optionalUser.get();
        if (user.getRole() == Role.DEANERY) {
            throw new IllegalArgumentException("У пользователя с ID " + userId + " уже есть роль Деканат.");
        }

        user.setRole(Role.DEANERY);
        return userRepository.save(user);
    }

    public User loadUserByUsername(String username) {
        return userRepository.findByEmail(username).orElseThrow(() -> new NoSuchElementException("Пользователь с почтой: " + username + " не найден"));
    }

    public User grantRole(Long userId, Role role) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new NoSuchElementException("Пользователь с ID " + userId + " не найден.");
        }

        User user = optionalUser.get();

        if (role == Role.DEANERY) {
            throw new NoSuchElementException("Назначение роли Деканат доступно только Админу.");
        }

        if (user.getRole() == role) {
            throw new IllegalArgumentException("У пользователя с ID " + userId + " уже есть роль " + role.getDisplayName() + ".");
        }

        user.setRole(role);
        return userRepository.save(user);
    }

    public List<UserDTO> getUsers(UserQueryType queryType) {
        List<User> users;

        if (queryType == null || queryType == UserQueryType.ALL) {
            users = userRepository.findAll();
        } else if (queryType == UserQueryType.TEACHERS) {
            users = userRepository.findAllByRole(Role.TEACHER);
        } else if (queryType == UserQueryType.STUDENTS) {
            users = userRepository.findAllByRole(Role.STUDENT);
        } else if (queryType == UserQueryType.WITHOUT_ROLE) {
            users = userRepository.findAllByRoleIsNull();
        } else {
            throw new IllegalArgumentException("Неизвестный тип запроса пользователей: " + queryType);
        }

        return users.stream()
                .map(userMapper::userToUserDto)
                .collect(Collectors.toList());
    }
}