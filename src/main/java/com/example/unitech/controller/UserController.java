package com.example.unitech.controller;

import com.example.unitech.DTO.UserDTO;
import com.example.unitech.payload.LoginPayload;
import com.example.unitech.payload.RegisterPayload;
import com.example.unitech.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> signup(@RequestBody @Valid RegisterPayload payload) {
        return ResponseEntity.ok(userService.register(payload));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginPayload payload) {
        return ResponseEntity.ok(userService.getToken(payload));
    }



}
