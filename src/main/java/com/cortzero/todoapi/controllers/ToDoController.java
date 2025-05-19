package com.cortzero.todoapi.controllers;

import com.cortzero.todoapi.dtos.CreateUpdateToDoDTO;
import com.cortzero.todoapi.dtos.ToDoDto;
import com.cortzero.todoapi.services.IToDoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class ToDoController {

    private final IToDoService toDoService;

    @PostMapping
    public ResponseEntity<Map<String, ToDoDto>> createToDo(@RequestBody @Valid CreateUpdateToDoDTO createToDoDTO) {
        return new ResponseEntity<>(
                Map.of("toDoCreated", toDoService.createToDoForCurrentUser(createToDoDTO)),
                HttpStatus.CREATED);
    }

}
