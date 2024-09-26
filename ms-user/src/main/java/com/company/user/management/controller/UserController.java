package com.company.user.management.controller;

import com.company.user.management.dto.UserCreateRequest;
import com.company.user.management.dto.UserResponse;
import com.company.user.management.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> saveUser(@RequestBody UserCreateRequest request) {
        return ResponseEntity.ok(userService.save(request));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> findUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

}
