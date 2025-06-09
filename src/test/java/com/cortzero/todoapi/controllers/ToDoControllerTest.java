package com.cortzero.todoapi.controllers;

import com.cortzero.todoapi.dtos.CreateUpdateToDoDTO;
import com.cortzero.todoapi.dtos.ToDoDto;
import com.cortzero.todoapi.security.SecurityConfiguration;
import com.cortzero.todoapi.services.IToDoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    @DisplayName("Test creating To-Do endpoint")
    void givenAuthenticatedUserAndValidRequest_whenCreateToDo_shouldReturnOkAndCreatedToDoDTO() throws Exception {
        // Given
        CreateUpdateToDoDTO createUpdateToDoDTO = giveCreatedUpdateToDoDTO("Do something");
        ToDoDto toDoDto = giveToDoDTO(1L, "Do something", false);

        when(toDoService.createToDoForCurrentUser(createUpdateToDoDTO)).thenReturn(toDoDto);

        // Then
        mockMvc.perform(post("/api/todos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createUpdateToDoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.task").value("Do something"))
                .andExpect(jsonPath("$.owner").value("testuser"));

        verify(toDoService, times(1)).createToDoForCurrentUser(createUpdateToDoDTO);
    }

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("Test changing To-Do incomplete status to complete status endpoint")
    void givenAuthenticatedUserAndIncompleteToDo_whenChangeToDoStatus_shouldReturnOkAndToDoDTOWithTrueValue() throws Exception {
        // Given
        ToDoDto toDoDto = giveToDoDTO(1L, "Do something", true);
        when(toDoService.changeToDoStatusForCurrentUser(1L)).thenReturn(toDoDto);

        // Then
        mockMvc.perform(patch("/api/todos/1/status")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.task").value("Do something"))
                .andExpect(jsonPath("$.owner").value("testuser"))
                .andExpect(jsonPath("$.complete").value(true));
    }

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("Test updating To-Do endpoint")
    void givenAuthenticatedUser_whenUpdateToDo_shouldReturnOkAndUpdatedToDoDTO() throws Exception {
        //Given
        CreateUpdateToDoDTO updateToDoDTO = giveCreatedUpdateToDoDTO("Do new things");
        ToDoDto updatedToDoDTO = giveToDoDTO(1L, "Do new things", false);

        when(toDoService.updateToDoForCurrentUser(1L, updateToDoDTO)).thenReturn(updatedToDoDTO);

        // Then
        mockMvc.perform(put("/api/todos/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updatedToDoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.task").value("Do new things"));
    }

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("Test deleting To-Do endpoint")
    void givenAuthenticatedUser_whenDeleteToDo_shouldReturnOkWithNoContent() throws Exception {
        // Then
        mockMvc.perform(delete("/api/todos/1")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("Test getting all To-Do tasks for current user endpoint")
    void givenAuthenticatedUser_whenGetAllToDos_shouldReturnOkAndListOfToDoDTOs() throws Exception {
        // Given
        List<ToDoDto> toDoDTOs = giveNToDoDTOs(3);
        when(toDoService.getAllToDosForCurrentUser()).thenReturn(toDoDTOs);

        // Then
        mockMvc.perform(get("/api/todos")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].task").value("Do task 1"))
                .andExpect(jsonPath("$[1].task").value("Do task 2"))
                .andExpect(jsonPath("$[2].task").value("Do task 3"));
    }

    private CreateUpdateToDoDTO giveCreatedUpdateToDoDTO(String task) {
        return CreateUpdateToDoDTO.builder()
                .task(task)
                .build();
    }

    private ToDoDto giveToDoDTO(Long id, String task, boolean status) {
        return ToDoDto.builder()
                .id(id)
                .task(task)
                .owner("testuser")
                .complete(status)
                .build();
    }

    private List<ToDoDto> giveNToDoDTOs(int n) {
        List<ToDoDto> toDoDTOs = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            toDoDTOs.add(giveToDoDTO((long) (i + 1) , "Do task " + (i + 1), false));
        }
        return toDoDTOs;
    }

}
