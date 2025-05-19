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
        ToDo toDo = giveToDo(user);

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

    private ToDo giveToDo(User user) {
        return ToDo.builder()
                .task("Do something")
                .completed(false)
                .user(user)
                .build();
    }

}
