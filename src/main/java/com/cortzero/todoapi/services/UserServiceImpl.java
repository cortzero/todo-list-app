package com.cortzero.todoapi.services;

import com.cortzero.todoapi.dtos.UpdateUserRequest;
import com.cortzero.todoapi.dtos.UserDto;
import com.cortzero.todoapi.entities.User;
import com.cortzero.todoapi.exceptions.ResourceNotFoundException;
import com.cortzero.todoapi.repositories.UserRepository;
import com.cortzero.todoapi.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;

    @Override
    public UserDto updateCurrentUserInformation(UpdateUserRequest userRequest) {
        return null;
    }

    private User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User with username '" + username + "' was not found."));
    }

    @Override
    public UserDto getCurrentUserInformation() {
        String username = securityUtils.getCurrentUserUsername();
        User loggedInUser = getByUsername(username);
        return UserDto.builder()
                .firstName(loggedInUser.getFirstName())
                .lastName(loggedInUser.getLastName())
                .username(loggedInUser.getUsername())
                .email(loggedInUser.getEmail())
                .build();
    }
}
