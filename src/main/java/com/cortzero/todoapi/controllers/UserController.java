package com.cortzero.todoapi.controllers;

import com.cortzero.todoapi.dtos.UpdateUserRequest;
import com.cortzero.todoapi.dtos.UserDto;
import com.cortzero.todoapi.services.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @PutMapping("account/update")
    public ResponseEntity<UserDto> updateCurrentUserInformation(@RequestBody UpdateUserRequest requestBody) {
        return new ResponseEntity<>(
                userService.updateCurrentUserInformation(requestBody),
                HttpStatus.OK);
    }

    @GetMapping("account")
    public ResponseEntity<UserDto> getCurrentUserInformation() {
        return new ResponseEntity<>(userService.getCurrentUserInformation(), HttpStatus.OK);
    }

}
