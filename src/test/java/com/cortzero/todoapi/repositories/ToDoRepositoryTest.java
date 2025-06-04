package com.cortzero.todoapi.repositories;

import com.cortzero.todoapi.entities.ToDo;
import com.cortzero.todoapi.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ToDoRepositoryTest {

    @Autowired
    private ToDoRepository toDoRepository;

    private User user;

    @BeforeEach
    void setup() {
        user = User.builder()
                .id(1L)
                .firstName("Test")
                .lastName("User")
                .username("testuser")
                .email("test@example.com")
                .password("123")
                .build();
    }

    @Test
    void givenValidToDoId_whenFindByUserAndId_shouldReturnToDoBelongingToUserAndWithGivenId() {
        // Given
        Long toDoId = 1L;

        // When
        Optional<ToDo> toDoAssociatedWithUser = toDoRepository.findByUserAndId(user, toDoId);

        // Then
        assertTrue(toDoAssociatedWithUser.isPresent());
        assertEquals("Do something", toDoAssociatedWithUser.get().getTask());
    }

    @Test
    void givenNonExistingToDoId_whenFindByUserAndId_shouldReturnEmptyOptional() {
        // Given
        Long toDoId = 1000L;

        // When
        Optional<ToDo> toDoAssociatedWithUser = toDoRepository.findByUserAndId(user, toDoId);

        // Then
        assertTrue(toDoAssociatedWithUser.isEmpty());
    }

    @Test
    void givenValidUser_whenFindByUser_shouldReturnListOfToDos() {
        // When
        List<ToDo> toDosCurrentUser = toDoRepository.findByUser(user);

        // Then
        assertNotNull(toDosCurrentUser);
        assertEquals(3, toDosCurrentUser.size());
        assertEquals("Do something", toDosCurrentUser.get(0).getTask());
        assertEquals("Do something else", toDosCurrentUser.get(1).getTask());
        assertEquals("Do another thing", toDosCurrentUser.get(2).getTask());
    }

    @Test
    void givenValidUser_whenDeleteByUserAndId_shouldDeleteTheCurrentUserToDoFromTheDatabase() {
        // When
        toDoRepository.deleteByUserAndId(user, 3L);

        // Then
        List<ToDo> toDosCurrentUser = toDoRepository.findByUser(user);
        assertEquals(2, toDosCurrentUser.size());
        assertEquals("Do something", toDosCurrentUser.get(0).getTask());
        assertEquals("Do something else", toDosCurrentUser.get(1).getTask());
    }

}
