package com.cortzero.todoapi.repositories;

import static org.junit.jupiter.api.Assertions.*;
import com.cortzero.todoapi.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private String username;
    private String email;
    private User user;

    @BeforeEach
    void setup() {
        username = "testuser";
        email = "test@example.com";
        user = User.builder()
                .firstName("Test")
                .lastName("User")
                .username(username)
                .email(email)
                .password("123")
                .build();
        entityManager.persist(user);
    }

    @Test
    void givenUsername_whenFindByUsername_shouldReturnOptionalWithUser() {
        //When
        Optional<User> foundUser = userRepository.findByUsername(username);

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals("Test", foundUser.get().getFirstName());
        assertEquals("User", foundUser.get().getLastName());
        assertEquals("test@example.com", foundUser.get().getEmail());
        assertEquals("123", foundUser.get().getPassword());
    }

    @Test
    void givenEmail_whenFindByEmail_shouldReturnOptionalWithUser() {
        // When
        Optional<User> foundUser = userRepository.findByEmail(email);

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals("Test", foundUser.get().getFirstName());
        assertEquals("User", foundUser.get().getLastName());
        assertEquals("test@example.com", foundUser.get().getEmail());
        assertEquals("123", foundUser.get().getPassword());
    }

}
