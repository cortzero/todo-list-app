package com.cortzero.todoapi.services;

import com.cortzero.todoapi.dtos.UpdateUserRequest;
import com.cortzero.todoapi.dtos.UserDto;

public interface IUserService {

    String USER_NOT_FOUND_MESSAGE = "User with username '%s' was not found.";

    UserDto updateCurrentUserInformation(UpdateUserRequest userRequest);
    UserDto getCurrentUserInformation();

}
