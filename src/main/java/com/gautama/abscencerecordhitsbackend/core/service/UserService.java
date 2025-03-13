package com.gautama.abscencerecordhitsbackend.core.service;

import com.gautama.abscencerecordhitsbackend.api.config.JwtUtil;
import com.gautama.abscencerecordhitsbackend.api.dto.UserDTO;
import com.gautama.abscencerecordhitsbackend.api.dto.UserFullDTO;
import com.gautama.abscencerecordhitsbackend.api.enums.Group;
import com.gautama.abscencerecordhitsbackend.api.enums.UserQueryType;
import com.gautama.abscencerecordhitsbackend.api.enums.Role;
import com.gautama.abscencerecordhitsbackend.api.mapper.UserMapper;
import com.gautama.abscencerecordhitsbackend.core.model.User;
import com.gautama.abscencerecordhitsbackend.core.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

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

    public List<UserDTO> getUsers(UserQueryType queryType, Group group) {
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

        if (group != null) {
            users = users.stream()
                    .filter(user -> group.equals(user.getStudentGroup()))
                    .collect(Collectors.toList());
        }

        return users.stream()
                .map(userMapper::userToUserDto)
                .collect(Collectors.toList());
    }

    public UserFullDTO getMe(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new BadCredentialsException("Неверный токен.");
        }

        String token = authHeader.substring(7);
        User user = loadUserByUsername(jwtUtil.extractUsername(token));
        return userMapper.userToUserFullDto(user);
    }
}