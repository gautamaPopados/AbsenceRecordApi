package com.gautama.abscencerecordhitsbackend.api.controller;

import com.gautama.abscencerecordhitsbackend.api.dto.UserDTO;
import com.gautama.abscencerecordhitsbackend.api.enums.Role;
import com.gautama.abscencerecordhitsbackend.api.enums.UserQueryType;
import com.gautama.abscencerecordhitsbackend.core.model.User;
import com.gautama.abscencerecordhitsbackend.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getUsers(
            @RequestParam(required = false) UserQueryType queryType
    ) {
        List<UserDTO> users = userService.getUsers(queryType);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{userId}/grant-dean-role")
    public ResponseEntity<User> grantDeanRole(@PathVariable Long userId) {
        User updatedUser = userService.grantDeanRole(userId);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/{userId}/grant-role")
    public ResponseEntity<User> grantRole(@PathVariable Long userId, @RequestParam Role role) {
        User updatedUser = userService.grantRole(userId, role);
        return ResponseEntity.ok(updatedUser);
    }
}