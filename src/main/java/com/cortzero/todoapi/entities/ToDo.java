package com.cortzero.todoapi.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "to_dos")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ToDo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "custom_to_dos_seq")
    @SequenceGenerator(name = "custom_to_dos_seq", sequenceName = "to_dos_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "task", nullable = false, columnDefinition = "text")
    @NotBlank(message = "The task attribute must not be empty or contain only whitespace characters.")
    private String task;

    @Column(name = "complete", nullable = false)
    private boolean complete;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    public String getUserUsername() {
        return user.getUsername();
    }

    public void changeStatus() {
        if (complete) {
            setComplete(false);
        }
        else {
            setComplete(true);
        }
    }

}