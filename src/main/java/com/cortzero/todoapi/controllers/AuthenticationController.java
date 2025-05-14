package com.cortzero.todoapi.controllers;

import com.cortzero.todoapi.dtos.CreateUserRequest;
import com.cortzero.todoapi.dtos.UserDto;
import com.cortzero.todoapi.services.IAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final IAuthenticationService authenticationService;

    @PostMapping("signup")
    public ResponseEntity<Map<String, UserDto>> registerUser(@RequestBody CreateUserRequest requestBody) {
        return new ResponseEntity<>(
                Map.of("userCreated", authenticationService.createUser(requestBody)),
                HttpStatus.CREATED);
    }

}
