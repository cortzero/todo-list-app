package com.cortzero.todoapi.services;

import com.cortzero.todoapi.dtos.CreateUpdateToDoDTO;
import com.cortzero.todoapi.dtos.ToDoDto;
import com.cortzero.todoapi.entities.ToDo;
import com.cortzero.todoapi.entities.User;
import com.cortzero.todoapi.exceptions.ResourceNotFoundException;
import com.cortzero.todoapi.repositories.ToDoRepository;
import com.cortzero.todoapi.repositories.UserRepository;
import com.cortzero.todoapi.security.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ToDoServiceImplTest {

    @Mock
    private ToDoRepository toDoRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private ToDoServiceImpl toDoService;

    @Test
    void givenValidAuthenticatedUser_whenCreateToDoForCurrentUser_shouldSaveToDoAndReturnDTO() {
        // Given
        CreateUpdateToDoDTO createUpdateToDoDTO = giveCreateUpdateToDoDTO();
        User user = giveUserEntity();
        ToDo toDo = giveToDo(user, "Do something");

        when(securityUtils.getCurrentUserUsername()).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(toDoRepository.save(any(ToDo.class))).thenReturn(toDo);

        // When
        ToDoDto toDoDto = toDoService.createToDoForCurrentUser(createUpdateToDoDTO);

        // Then
        assertNotNull(toDoDto);
        assertEquals("Do something", toDoDto.getTask());
        assertEquals("testuser", toDoDto.getOwner());
    }

    @Test
    void givenNonExistingUser_whenCreateToDoForCurrentUser_shouldThrowAnException() {
        // Given
        CreateUpdateToDoDTO createUpdateToDoDTO = giveCreateUpdateToDoDTO();
        String username = "no_user";

        when(securityUtils.getCurrentUserUsername()).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Then
        assertThrows(
                ResourceNotFoundException.class,
                () -> toDoService.createToDoForCurrentUser(createUpdateToDoDTO),
                String.format(IUserService.USER_NOT_FOUND_MESSAGE, username));
    }

    @Test
    void givenValidAuthenticatedUser_whenGetAllToDosForCurrentUser_shouldReturnListOfToDoDTOs() {
        // Given
        User user = giveUserEntity();
        List<ToDo> toDos = giveNToDosForCurrentUser(user, 3);

        when(securityUtils.getCurrentUserUsername()).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(toDoRepository.findByUser(user)).thenReturn(toDos);

        // When
        List<ToDoDto> toDoDTOs = toDoService.getAllToDosForCurrentUser();

        // Then
        assertNotNull(toDoDTOs);
        assertEquals(3, toDoDTOs.size());
        assertEquals("Do task 1", toDoDTOs.get(0).getTask());
        assertEquals("Do task 2", toDoDTOs.get(1).getTask());
        assertEquals("Do task 3", toDoDTOs.get(2).getTask());
    }

    private CreateUpdateToDoDTO giveCreateUpdateToDoDTO() {
        return CreateUpdateToDoDTO.builder()
                .task("Do something")
                .build();
    }

    private User giveUserEntity() {
        return User.builder()
                .id(1L)
                .firstName("First Name")
                .lastName("Last Name")
                .username("testuser")
                .email("test@example.com")
                .password("123")
                .build();
    }

    private ToDo giveToDo(User user, String task) {
        return ToDo.builder()
                .task(task)
                .completed(false)
                .user(user)
                .build();
    }

    private List<ToDo> giveNToDosForCurrentUser(User user, int n) {
        List<ToDo> toDos = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            toDos.add(giveToDo(user, "Do task " + (i + 1)));
        }
        return toDos;
    }

}
