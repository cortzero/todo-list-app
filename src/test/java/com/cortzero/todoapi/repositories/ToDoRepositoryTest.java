package com.cortzero.todoapi.repositories;

import com.cortzero.todoapi.entities.ToDo;
import com.cortzero.todoapi.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class ToDoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ToDoRepository toDoRepository;

    private User user;

    @BeforeEach
    void setup() {
        user = User.builder()
                .firstName("Test")
                .lastName("User")
                .username("testuser")
                .email("test@example.com")
                .password("123")
                .build();
        entityManager.persist(user);

        ToDo toDo1 = ToDo.builder()
                .task("Do something")
                .complete(false)
                .user(user)
                .build();
        entityManager.persist(toDo1);

        ToDo toDo2 = ToDo.builder()
                .task("Do something else")
                .complete(false)
                .user(user)
                .build();
        entityManager.persist(toDo2);

        ToDo toDo3 = ToDo.builder()
                .task("Do another thing")
                .complete(false)
                .user(user)
                .build();
        entityManager.persist(toDo3);
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

}
