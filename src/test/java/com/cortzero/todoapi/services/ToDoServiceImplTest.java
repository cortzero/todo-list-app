package com.cortzero.todoapi.services;

import com.cortzero.todoapi.dtos.CreateUpdateToDoDTO;
import com.cortzero.todoapi.dtos.ToDoDto;
import com.cortzero.todoapi.entities.ToDo;
import com.cortzero.todoapi.entities.User;
import com.cortzero.todoapi.exceptions.ResourceNotFoundException;
import com.cortzero.todoapi.repositories.ToDoRepository;
import com.cortzero.todoapi.repositories.UserRepository;
import com.cortzero.todoapi.security.SecurityUtils;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("Test creating a To-Do task given an authenticated user")
    void givenValidAuthenticatedUser_whenCreateToDoForCurrentUser_shouldSaveToDoAndReturnDTO() {
        // Given
        CreateUpdateToDoDTO createUpdateToDoDTO = giveCreateUpdateToDoDTO();
        User user = giveUserEntity();
        ToDo toDo = giveToDo(user, "Do something", false);

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
    @DisplayName("Test creating a To-Do task given a non-existing authenticated user")
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
    @DisplayName("Test changing a To-Do task incomplete status to complete given an authenticated user")
    void givenAuthenticatedUserAndIncompleteToDo_whenChangeToDoStatusForCurrentUser_shouldReturnDtoWithCompleteStatus() {
        // Given
        User user = giveUserEntity();
        ToDo incompleteToDo = giveToDoWithId(1L, user, "Do something", false);
        ToDo completeToDo = giveToDoWithId(1L, user, "Do something", true);

        when(securityUtils.getCurrentUserUsername()).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(toDoRepository.findByUserAndId(user, 1L)).thenReturn(Optional.of(incompleteToDo)); // has complete = false
        when(toDoRepository.save(any(ToDo.class))).thenReturn(completeToDo); // has complete = true

        // When
        ToDoDto toDoDto = toDoService.changeToDoStatusForCurrentUser(1L);

        // Then
        assertNotNull(toDoDto);
        assertTrue(toDoDto.isComplete());
    }

    @Test
    @DisplayName("Test changing a To-Do task complete status to incomplete given an authenticated user")
    void givenAuthenticatedUserAndCompleteToDo_whenChangeToDoStatusForCurrentUser_shouldReturnDtoWithIncompleteStatus() {
        // Given
        User user = giveUserEntity();
        ToDo completeToDo = giveToDoWithId(1L, user, "Do something", true);
        ToDo incompleteToDo = giveToDoWithId(1L, user, "Do something", false);

        when(securityUtils.getCurrentUserUsername()).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(toDoRepository.findByUserAndId(user, 1L)).thenReturn(Optional.of(completeToDo)); // has complete = true
        when(toDoRepository.save(any(ToDo.class))).thenReturn(incompleteToDo); // has complete = false

        // When
        ToDoDto toDoDto = toDoService.changeToDoStatusForCurrentUser(1L);

        // Then
        assertNotNull(toDoDto);
        assertFalse(toDoDto.isComplete());
    }

    @Test
    @DisplayName("Test getting all To-Do tasks given an authenticated user")
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

    @Test
    @DisplayName("Test updating a To-Do task given an authenticated user")
    void givenValidAuthenticatedUser_whenUpdateToDoForCurrentUser_shouldReturnUpdatedToDoDTO() {
        // Given
        User user = giveUserEntity();
        ToDo toDo = giveToDoWithId(1L, user, "Do something", false);
        ToDo toDoUpdated = giveToDoWithId(1L, user, "New things to do", false);
        CreateUpdateToDoDTO updateToDoDTO = CreateUpdateToDoDTO.builder()
                .task("New things to do")
                .build();

        when(securityUtils.getCurrentUserUsername()).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(toDoRepository.findByUserAndId(user, 1L)).thenReturn(Optional.of(toDo));
        when(toDoRepository.save(any(ToDo.class))).thenReturn(toDoUpdated);

        // When
        ToDoDto toDoDto = toDoService.updateToDoForCurrentUser(1L, updateToDoDTO);

        // Then
        assertNotNull(toDoDto);
        assertEquals("New things to do", toDoDto.getTask());
    }

    @Test
    @DisplayName("Test updating a To-Do task given an authenticated user but non-existing To-Do task")
    void givenNonExistingToDo_whenUpdateToDoForCurrentUser_shouldThrowResourceNotFoundException() {
        // Given
        User user = giveUserEntity();
        CreateUpdateToDoDTO updateToDoDTO = CreateUpdateToDoDTO.builder()
                .task("New things to do")
                .build();

        when(securityUtils.getCurrentUserUsername()).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(toDoRepository.findByUserAndId(user, 1L)).thenReturn(Optional.empty());

        // Then
        assertThrows(ResourceNotFoundException.class,
                () -> toDoService.updateToDoForCurrentUser(1L, updateToDoDTO));
    }

    @Test
    @DisplayName("Test deleting a To-Do task given an authenticated user")
    void givenValidAuthenticatedUser_whenDeleteToDoForCurrentUser_shouldDeleteToDoFromDatabase() {
        // Given
        User user = giveUserEntity();
        when(securityUtils.getCurrentUserUsername()).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        // Then
        assertDoesNotThrow(() -> toDoService.deleteToDoForCurrentUser(1L));
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

    private ToDo giveToDo(User user, String task, boolean status) {
        return ToDo.builder()
                .task(task)
                .complete(status)
                .user(user)
                .build();
    }

    private ToDo giveToDoWithId(Long id, User user, String task, boolean status) {
        return ToDo.builder()
                .id(id)
                .task(task)
                .complete(status)
                .user(user)
                .build();
    }

    private List<ToDo> giveNToDosForCurrentUser(User user, int n) {
        List<ToDo> toDos = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            toDos.add(giveToDo(user, "Do task " + (i + 1), false));
        }
        return toDos;
    }

}
