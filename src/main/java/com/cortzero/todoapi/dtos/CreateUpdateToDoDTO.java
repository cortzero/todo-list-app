package com.cortzero.todoapi.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CreateUpdateToDoDTO {

    @NotBlank
    private String task;

}
