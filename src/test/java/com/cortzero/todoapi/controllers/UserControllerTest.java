package com.cortzero.todoapi.controllers;

import com.cortzero.todoapi.dtos.UpdateUserRequest;
import com.cortzero.todoapi.dtos.UserDto;
import com.cortzero.todoapi.security.SecurityConfiguration;
import com.cortzero.todoapi.services.IUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(SecurityConfiguration.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    @MockitoBean
    private IUserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "testuser")
    void givenAuthenticatedUserAndValidRequest_whenUpdateCurrentUserInformation_returnsOkAndUpdatedUser() throws Exception {
        // Given
        UpdateUserRequest updateUserRequest = UpdateUserRequest.builder()
                .firstName("First Name Updated")
                .lastName("Last Name Updated")
                .email("updated@example.com")
                .build();

        UserDto userDto = UserDto.builder()
                .firstName("First Name Updated")
                .lastName("Last Name Updated")
                .username("testuser")
                .email("updated@example.com")
                .build();

        when(userService.updateCurrentUserInformation(any(UpdateUserRequest.class))).thenReturn(userDto);

        // Then
        mockMvc.perform(put("/api/users/account/update")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateUserRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("First Name Updated"))
                .andExpect(jsonPath("$.lastName").value("Last Name Updated"))
                .andExpect(jsonPath("$.email").value("updated@example.com"));

        verify(userService, times(1)).updateCurrentUserInformation(any(UpdateUserRequest.class));
    }

    @Test
    @WithMockUser(username = "testuser")
    void givenAuthenticatedUser_whenGetCurrentUserInformation__returnsOkAndCurrentUser() throws Exception {
        // Given
        UserDto userDto = UserDto.builder()
                .firstName("First Name")
                .lastName("Last Name")
                .username("testuser")
                .email("test@example.com")
                .build();

        when(userService.getCurrentUserInformation()).thenReturn(userDto);

        // Then
        mockMvc.perform(get("/api/users/account")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("First Name"))
                .andExpect(jsonPath("$.lastName").value("Last Name"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

}
