package com.cortzero.todoapi.services;

import com.cortzero.todoapi.dtos.UpdateUserRequest;
import com.cortzero.todoapi.dtos.UserDto;
import org.springframework.stereotype.Service;

@Service
public interface IUserService {

    UserDto updateCurrentUserInformation(UpdateUserRequest userRequest);
    UserDto getCurrentUserInformation();

}
