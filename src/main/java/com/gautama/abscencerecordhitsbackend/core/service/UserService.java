package com.gautama.abscencerecordhitsbackend.core.service;

import com.gautama.abscencerecordhitsbackend.api.dto.UserDTO;
import com.gautama.abscencerecordhitsbackend.api.enums.UserQueryType;
import com.gautama.abscencerecordhitsbackend.api.enums.UserRole;
import com.gautama.abscencerecordhitsbackend.api.mapper.UserMapper;
import com.gautama.abscencerecordhitsbackend.core.model.User;
import com.gautama.abscencerecordhitsbackend.core.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь с ID " + userId + " не найден.");
        }

        User user = optionalUser.get();
        if (user.getRole() == Role.DEANERY) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "У пользователя с ID " + userId + " уже есть роль Деканат.");
        }

        user.setRole(Role.DEANERY);
        return userRepository.save(user);
    }

    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }
    public User grantRole(Long userId, Role role) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь с ID " + userId + " не найден.");
        }

        User user = optionalUser.get();

        if (role == Role.DEANERY) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Назначение роли Деканат доступно только Админу.");
        }

        if (user.getRole() == role) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "У пользователя с ID " + userId + " уже есть роль " + role.getDisplayName() + ".");
        }

        user.setRole(role);
        return userRepository.save(user);
    }

    public List<UserDTO> getUsers(UserQueryType queryType) {
        List<User> users;

        if (queryType == null || queryType == UserQueryType.ALL) {
            users = userRepository.findAll();
        } else if (queryType == UserQueryType.TEACHERS) {
            users = userRepository.findAllByUserRole(UserRole.TEACHER);
        } else if (queryType == UserQueryType.STUDENTS) {
            users = userRepository.findAllByUserRole(UserRole.STUDENT);
        } else if (queryType == UserQueryType.WITHOUT_ROLE) {
            users = userRepository.findAllByUserRoleIsNull();
        } else {
            throw new IllegalArgumentException("Неизвестный тип запроса пользователей: " + queryType);
        }

        return users.stream()
                .map(userMapper::userToUserDto)
                .collect(Collectors.toList());
    }
}