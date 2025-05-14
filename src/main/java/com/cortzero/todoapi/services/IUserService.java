package com.cortzero.todoapi.services;

import com.cortzero.todoapi.dtos.UpdateUserRequest;
import com.cortzero.todoapi.dtos.UserDto;

public interface IUserService {

    UserDto updateCurrentUserInformation(UpdateUserRequest userRequest);
    UserDto getCurrentUserInformation();

}
