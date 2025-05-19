package com.cortzero.todoapi.controllers;

import com.cortzero.todoapi.dtos.CreateUpdateToDoDTO;
import com.cortzero.todoapi.dtos.ToDoDto;
import com.cortzero.todoapi.security.SecurityConfiguration;
import com.cortzero.todoapi.services.IToDoService;
import com.fasterxml.jackson.core.JsonProcessingException;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ToDoController.class)
@Import(SecurityConfiguration.class)
@AutoConfigureMockMvc
public class ToDoControllerTest {

    @MockitoBean
    private IToDoService toDoService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "testuser")
    void givenAuthenticatedUserAndValidRequest_whenCreateToDo_returnsOkAndCreatedToDoDTO() throws Exception {
        // Given
        CreateUpdateToDoDTO createUpdateToDoDTO = giveCreatedUpdateToDoDTO();
        ToDoDto toDoDto = giveToDoDTO();

        when(toDoService.createToDoForCurrentUser(createUpdateToDoDTO)).thenReturn(toDoDto);

        // Then
        mockMvc.perform(post("/api/todos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createUpdateToDoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.toDoCreated.task").value("Do something"))
                .andExpect(jsonPath("$.toDoCreated.owner").value("testuser"));

        verify(toDoService, times(1)).createToDoForCurrentUser(createUpdateToDoDTO);
    }

    private CreateUpdateToDoDTO giveCreatedUpdateToDoDTO() {
        return CreateUpdateToDoDTO.builder()
                .task("Do something")
                .build();
    }

    private ToDoDto giveToDoDTO() {
        return ToDoDto.builder()
                .task("Do something")
                .owner("testuser")
                .build();
    }

}
