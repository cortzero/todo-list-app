package com.cortzero.todoapi.services;

import com.cortzero.todoapi.dtos.CreateUpdateToDoDTO;
import com.cortzero.todoapi.dtos.ToDoDto;
import com.cortzero.todoapi.entities.ToDo;
import com.cortzero.todoapi.entities.User;
import com.cortzero.todoapi.exceptions.ResourceNotFoundException;
import com.cortzero.todoapi.repositories.ToDoRepository;
import com.cortzero.todoapi.repositories.UserRepository;
import com.cortzero.todoapi.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ToDoServiceImpl implements IToDoService {

    private final ToDoRepository toDoRepository;
    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;

    @Override
    public ToDoDto createToDoForCurrentUser(CreateUpdateToDoDTO createToDoDTO) {
        String username = securityUtils.getCurrentUserUsername();
        User loggedInUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(IUserService.USER_NOT_FOUND_MESSAGE, username)));
        ToDo toDo = ToDo.builder()
                .task(createToDoDTO.getTask())
                .complete(false)
                .user(loggedInUser)
                .build();
        toDoRepository.save(toDo);
        return mapToToDoDto(toDo);
    }

    @Override
    public ToDoDto changeToDoStatusForCurrentUser(Long toDoId) {
        String username = securityUtils.getCurrentUserUsername();
        User loggedInUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(IUserService.USER_NOT_FOUND_MESSAGE, username)));
        ToDo toDo = toDoRepository.findByUserAndId(loggedInUser, toDoId)
                .orElseThrow(() -> new ResourceNotFoundException("The To-Do was not found."));
        toDo.changeStatus();
        return mapToToDoDto(toDoRepository.save(toDo));
    }

    @Override
    public List<ToDoDto> getAllToDosForCurrentUser() {
        String username = securityUtils.getCurrentUserUsername();
        User loggedInUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(IUserService.USER_NOT_FOUND_MESSAGE, username)));
        List<ToDo> toDosForUser = toDoRepository.findByUser(loggedInUser);
        return toDosForUser.stream()
                .map(this::mapToToDoDto)
                .toList();
    }

    private ToDoDto mapToToDoDto(ToDo toDo) {
        return ToDoDto.builder()
                .task(toDo.getTask())
                .owner(toDo.getUser().getUsername())
                .complete(toDo.isComplete())
                .build();
    }

}
