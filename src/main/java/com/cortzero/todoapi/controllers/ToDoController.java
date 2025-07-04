package com.cortzero.todoapi.controllers;

import com.cortzero.todoapi.dtos.CreateUpdateToDoDTO;
import com.cortzero.todoapi.dtos.ToDoDto;
import com.cortzero.todoapi.services.IToDoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class ToDoController {

    private final IToDoService toDoService;

    @PostMapping
    public ResponseEntity<ToDoDto> createToDo(@RequestBody @Valid CreateUpdateToDoDTO createToDoDTO) {
        return new ResponseEntity<>(
                toDoService.createToDoForCurrentUser(createToDoDTO),
                HttpStatus.CREATED);
    }

    @PatchMapping("{toDoId}/status")
    public ResponseEntity<ToDoDto> changeToDoStatus(@PathVariable Long toDoId) {
        return ResponseEntity.ok(toDoService.changeToDoStatusForCurrentUser(toDoId));
    }

    @PutMapping("{toDoId}")
    public ResponseEntity<ToDoDto> updateToDo(@PathVariable Long toDoId, @RequestBody @Valid CreateUpdateToDoDTO createUpdateToDoDTO) {
        return ResponseEntity.ok(toDoService.updateToDoForCurrentUser(toDoId, createUpdateToDoDTO));
    }

    @DeleteMapping("{toDoId}")
    public ResponseEntity<Void> deleteToDo(@PathVariable Long toDoId) {
        toDoService.deleteToDoForCurrentUser(toDoId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ToDoDto>> getAllToDos() {
        return new ResponseEntity<>(
                toDoService.getAllToDosForCurrentUser(),
                HttpStatus.OK);
    }

}
