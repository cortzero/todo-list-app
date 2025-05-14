package com.cortzero.todoapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserDto {

    private String firstName;
    private String lastName;
    private String username;
    private String email;

}
