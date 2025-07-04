package com.cortzero.todoapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ToDoDto {

    private Long id;
    private String task;
    private String owner;
    private boolean complete;

}
