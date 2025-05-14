package com.cortzero.todoapi.services;

import com.cortzero.todoapi.dtos.CreateUserRequest;
import com.cortzero.todoapi.dtos.UserDto;

public interface IAuthenticationService {

    UserDto createUser(CreateUserRequest userRequest);

}
