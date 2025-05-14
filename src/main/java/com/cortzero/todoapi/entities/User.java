package com.cortzero.todoapi.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "custom_users_seq")
    @SequenceGenerator(name = "custom_users_seq", sequenceName = "users_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 20)
    @NotBlank
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 20)
    @NotBlank
    private String lastName;

    @Column(name = "username", nullable = false, unique = true, length = 20)
    @NotBlank
    private String username;

    @Column(name = "email", nullable = false, unique = true, length = 30)
    @Email
    private String email;

    @Column(name = "password", nullable = false)
    @NotBlank
    private String password;

}