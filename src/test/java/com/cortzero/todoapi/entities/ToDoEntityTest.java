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
    void shouldUpdateCompletedStatus() {
        ToDo toDo = ToDo.builder().task("Do homework").completed(false).build();
        toDo.completeToDo();
        assertTrue(toDo.isCompleted());
    }

}
