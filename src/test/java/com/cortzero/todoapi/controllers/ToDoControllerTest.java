package com.cortzero.todoapi.controllers;

import com.cortzero.todoapi.dtos.CreateUpdateToDoDTO;
import com.cortzero.todoapi.dtos.ToDoDto;
import com.cortzero.todoapi.security.SecurityConfiguration;
import com.cortzero.todoapi.services.IToDoService;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    void givenAuthenticatedUserAndValidRequest_whenCreateToDo_shouldReturnOkAndCreatedToDoDTO() throws Exception {
        // Given
        CreateUpdateToDoDTO createUpdateToDoDTO = giveCreatedUpdateToDoDTO("Do something");
        ToDoDto toDoDto = giveToDoDTO("Do something");

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

    @Test
    @WithMockUser(username = "testuser")
    void givenAuthenticatedUser_whenGetAllToDos_shouldReturnOkAndListOfToDoDTOs() throws Exception {
        // Given
        List<ToDoDto> toDoDTOs = giveNToDoDTOs(3);
        when(toDoService.getAllToDosForCurrentUser()).thenReturn(toDoDTOs);

        // Then
        mockMvc.perform(get("/api/todos")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.toDos.length()").value(3))
                .andExpect(jsonPath("$.toDos[0].task").value("Do task 1"))
                .andExpect(jsonPath("$.toDos[1].task").value("Do task 2"))
                .andExpect(jsonPath("$.toDos[2].task").value("Do task 3"));
    }

    private CreateUpdateToDoDTO giveCreatedUpdateToDoDTO(String task) {
        return CreateUpdateToDoDTO.builder()
                .task(task)
                .build();
    }

    private ToDoDto giveToDoDTO(String task) {
        return ToDoDto.builder()
                .task(task)
                .owner("testuser")
                .build();
    }

    private List<ToDoDto> giveNToDoDTOs(int n) {
        List<ToDoDto> toDoDTOs = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            toDoDTOs.add(giveToDoDTO("Do task " + (i + 1)));
        }
        return toDoDTOs;
    }

}
