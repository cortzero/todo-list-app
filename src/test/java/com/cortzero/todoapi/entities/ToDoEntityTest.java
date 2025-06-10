package com.cortzero.todoapi.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ToDoEntityTest {

    @Test
    @DisplayName("Test returning the owner's username of a To-Do task")
    void shouldReturnUserUsername() {
        User user = User.builder().username("testuser").build();
        ToDo toDo = ToDo.builder().task("Do test task").user(user).build();

        assertEquals("testuser", toDo.getUserUsername());
    }

    @Test
    @DisplayName("Test changing To-Do task incomplete status to complete status")
    void givenToDoWithCompleteFieldSetToFalse_whenChangeStatus_shouldChangeCompleteFieldToTrue() {
        // Given
        ToDo toDo = ToDo.builder().task("Do homework").complete(false).build();

        // When
        toDo.changeStatus();

        // Then
        assertTrue(toDo.isComplete());
    }

    @Test
    @DisplayName("Test changing To-Do task complete status to incomplete status")
    void givenToDoWithCompleteFieldSetToTrue_whenChangeStatus_shouldChangeCompleteFieldToFalse() {
        // Given
        ToDo toDo = ToDo.builder().task("Do homework").complete(true).build();

        // When
        toDo.changeStatus();

        // Then
        assertFalse(toDo.isComplete());
    }

}
