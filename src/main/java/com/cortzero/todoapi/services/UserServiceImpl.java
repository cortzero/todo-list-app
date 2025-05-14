package com.cortzero.todoapi.services;

import com.cortzero.todoapi.dtos.UpdateUserRequest;
import com.cortzero.todoapi.dtos.UserDto;
import com.cortzero.todoapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;

    @Override
    public UserDto updateCurrentUserInformation(UpdateUserRequest userRequest) {
        return null;
    }

    @Override
    public UserDto getCurrentUserInformation() {
        return null;
    }
}
