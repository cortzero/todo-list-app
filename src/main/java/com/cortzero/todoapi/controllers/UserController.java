package com.cortzero.todoapi.controllers;

import com.cortzero.todoapi.dtos.UserDto;
import com.cortzero.todoapi.services.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @GetMapping("account")
    public ResponseEntity<Map<String, UserDto>> getCurrentUserInformation() {
        return new ResponseEntity<>(Map.of("user", userService.getCurrentUserInformation()), HttpStatus.OK);
    }

}
