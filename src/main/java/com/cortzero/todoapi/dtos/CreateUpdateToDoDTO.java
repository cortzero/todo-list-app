package com.cortzero.todoapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CreateUpdateToDoDTO {

    private String task;

}
