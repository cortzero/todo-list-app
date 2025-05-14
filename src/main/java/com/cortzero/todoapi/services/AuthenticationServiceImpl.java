package com.cortzero.todoapi.services;

import com.cortzero.todoapi.dtos.CreateUserRequest;
import com.cortzero.todoapi.dtos.UserDto;
import com.cortzero.todoapi.entities.User;
import com.cortzero.todoapi.exceptions.UsernameAlreadyExistsException;
import com.cortzero.todoapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements IAuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto createUser(CreateUserRequest userRequest) {
        Optional<User> userOptional = userRepository.findByUsername(userRequest.getUsername());
        if (userOptional.isEmpty()) {
            User user = User.builder()
                    .firstName(userRequest.getFirstName())
                    .lastName(userRequest.getLastName())
                    .username(userRequest.getUsername())
                    .email(userRequest.getEmail())
                    .password(passwordEncoder.encode(userRequest.getPassword()))
                    .build();
            userRepository.save(user);
            return UserDto.builder()
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .build();
        }
        else {
            throw new UsernameAlreadyExistsException("Username '" + userRequest.getUsername() + "' already exists.");
        }
    }
}
