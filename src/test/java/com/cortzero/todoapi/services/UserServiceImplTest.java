package com.cortzero.todoapi.services;

import com.cortzero.todoapi.dtos.UpdateUserRequest;
import com.cortzero.todoapi.dtos.UserDto;
import com.cortzero.todoapi.entities.User;
import com.cortzero.todoapi.exceptions.ResourceNotFoundException;
import com.cortzero.todoapi.exceptions.UserEmailAlreadyExistsException;
import com.cortzero.todoapi.repositories.UserRepository;
import com.cortzero.todoapi.security.SecurityUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Test updating user information given authenticated user and valid input")
    void givenExistingAuthenticatedUserAndValidRequest_whenUpdateCurrentUserInformation_updatesAndReturnsDto() {
        // Given
        UpdateUserRequest updateUserRequest = UpdateUserRequest.builder()
                .firstName("Name Updated")
                .lastName("Last Name Updated")
                .email("updated@example.com")
                .build();

        User existingUser = User.builder()
                .id(1L)
                .firstName("Test")
                .lastName("User")
                .username("testuser")
                .email("test@example.com")
                .password("123")
                .build();

        when(securityUtils.getCurrentUserUsername()).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(existingUser));
        when(userRepository.findByEmail("updated@example.com")).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(User.builder()
                .id(1L)
                .firstName("Name Updated")
                .lastName("Last Name Updated")
                .username("testuser")
                .email("updated@example.com")
                .password("123")
                .build()
        );

        // When
        UserDto updatedUserDto = userService.updateCurrentUserInformation(updateUserRequest);

        // Then
        assertNotNull(updatedUserDto);
        assertEquals("Name Updated", updatedUserDto.getFirstName());
        assertEquals("Last Name Updated", updatedUserDto.getLastName());
        assertEquals("updated@example.com", updatedUserDto.getEmail());
        verify(userRepository, times(1)).findByUsername("testuser");
        verify(userRepository, times(1)).findByEmail("updated@example.com");
        verify(userRepository, times(1)).save(any(User.class));
        verify(securityUtils, times(1)).getCurrentUserUsername();
    }

    @Test
    @DisplayName("Test updating user information given authenticated user and invalid input with repeated email")
    void givenExistingAuthenticatedUserAndRepeatedEmail_whenUpdateCurrentUserInformation_shouldThrowException() {
        // Given
        String username = "testuser";
        UpdateUserRequest updateUserRequest = UpdateUserRequest.builder()
                .firstName("Name Updated")
                .lastName("Last Name Updated")
                .email("test@example.com")
                .build();

        User anotherUserWithEmail = User.builder()
                .id(1L)
                .firstName("Test")
                .lastName("User")
                .username("another_testuser")
                .email("test@example.com")
                .password("123")
                .build();

        when(securityUtils.getCurrentUserUsername()).thenReturn(username);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(anotherUserWithEmail));

        // Then
        assertThrows(
                UserEmailAlreadyExistsException.class,
                () -> userService.updateCurrentUserInformation(updateUserRequest),
                "The email '" + updateUserRequest.getEmail() + "' is already taken.");
    }

    @Test
    @DisplayName("Test updating user information given non existing user")
    void givenNonExistingAuthenticatedUser_whenUpdateCurrentUserInformation_shouldThrowException() {
        // Given
        String username = "testuser";
        UpdateUserRequest updateUserRequest = UpdateUserRequest.builder()
                .firstName("Name Updated")
                .lastName("Last Name Updated")
                .email("test@example.com")
                .build();

        when(securityUtils.getCurrentUserUsername()).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        // Then
        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.updateCurrentUserInformation(updateUserRequest),
                String.format(IUserService.USER_NOT_FOUND_MESSAGE, username));
    }

    @Test
    @DisplayName("Test getting user information given existing authenticated user")
    void givenExistingAuthenticatedUser_whenGetCurrentUserInformation_shouldReturnUserDto() {
        // Given
        String username = "testuser";
        User loggedInUser = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .username(username)
                .email("john@example.com")
                .password("123")
                .build();

        when(securityUtils.getCurrentUserUsername()).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(loggedInUser));

        // When
        UserDto userDto = userService.getCurrentUserInformation();

        // Then
        assertNotNull(userDto);
        assertEquals("John", userDto.getFirstName());
        assertEquals("Doe", userDto.getLastName());
        assertEquals(username, userDto.getUsername());
        assertEquals("john@example.com", userDto.getEmail());
    }

    @Test
    @DisplayName("Test getting user information given non existing authenticated user")
    void givenNonExistingAuthenticatedUser_whenGetCurrentUserInformation_shouldThrowException() {
        // Given
        String username = "testuser";
        when(securityUtils.getCurrentUserUsername()).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Then
        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.getCurrentUserInformation(),
                String.format(IUserService.USER_NOT_FOUND_MESSAGE, username));

    }

}
