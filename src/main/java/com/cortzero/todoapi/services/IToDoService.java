package com.cortzero.todoapi.services;

import com.cortzero.todoapi.dtos.CreateUpdateToDoDTO;
import com.cortzero.todoapi.dtos.ToDoDto;

import java.util.List;

public interface IToDoService {

    ToDoDto createToDoForCurrentUser(CreateUpdateToDoDTO createToDoDTO);
    ToDoDto changeToDoStatusForCurrentUser(Long toDoId);
    ToDoDto updateToDoForCurrentUser(Long toDoId, CreateUpdateToDoDTO updateToDoDTO);
//    void deleteToDoForCurrentUser(Long toDoId);
    List<ToDoDto> getAllToDosForCurrentUser();

}
