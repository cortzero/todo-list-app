package com.cortzero.todoapi.services;

import com.cortzero.todoapi.dtos.UpdateUserRequest;
import com.cortzero.todoapi.dtos.UserDto;
import com.cortzero.todoapi.entities.User;
import com.cortzero.todoapi.exceptions.ResourceNotFoundException;
import com.cortzero.todoapi.exceptions.UserEmailAlreadyExistsException;
import com.cortzero.todoapi.repositories.UserRepository;
import com.cortzero.todoapi.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;

    @Override
    public UserDto updateCurrentUserInformation(UpdateUserRequest userRequest) {
        String username = securityUtils.getCurrentUserUsername();
        Optional<User> userOptional = userRepository.findByEmail(userRequest.getEmail());
        if (userOptional.isPresent() && !userOptional.get().getUsername().equals(username)) {
            throw new UserEmailAlreadyExistsException("The email '" + userRequest.getEmail() + "' is already taken.");
        }
        else {
            return userRepository.findByUsername(username)
                    .map(existingUser -> {
                        existingUser.setFirstName(userRequest.getFirstName());
                        existingUser.setLastName(userRequest.getLastName());
                        existingUser.setEmail(userRequest.getEmail());
                        userRepository.save(existingUser);
                        return UserDto.builder()
                                .firstName(existingUser.getFirstName())
                                .lastName(existingUser.getLastName())
                                .username(existingUser.getUsername())
                                .email(existingUser.getEmail())
                                .build();
                    })
                    .orElseThrow(() -> new ResourceNotFoundException(
                            String.format(IUserService.USER_NOT_FOUND_MESSAGE, username))
                    );
        }
    }

    private User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(IUserService.USER_NOT_FOUND_MESSAGE, username)));
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
