package com.cortzero.todoapi.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ToDoEntityTest {

    @Test
    void shouldReturnUserUsername() {
        User user = User.builder().username("testuser").build();
        ToDo toDo = ToDo.builder().task("Do test task").user(user).build();

        assertEquals("testuser", toDo.getUserUsername());
    }

    @Test
    void givenToDoWithCompleteFieldSetToFalse_whenChangeStatus_shouldChangeCompleteFieldToTrue() {
        // Given
        ToDo toDo = ToDo.builder().task("Do homework").complete(false).build();

        // When
        toDo.changeStatus();

        // Then
        assertTrue(toDo.isComplete());
    }

    @Test
    void givenToDoWithCompleteFieldSetToTrue_whenChangeStatus_shouldChangeCompleteFieldToFalse() {
        // Given
        ToDo toDo = ToDo.builder().task("Do homework").complete(true).build();

        // When
        toDo.changeStatus();

        // Then
        assertFalse(toDo.isComplete());
    }

}
