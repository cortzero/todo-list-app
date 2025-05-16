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
    @NotBlank
    private String task;

    @Column(name = "completed", nullable = false)
    private boolean completed;

    @ManyToOne(cascade = CascadeType.REMOVE, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    public String getUserUsername() {
        return user.getUsername();
    }

    public void completeToDo() {
        setCompleted(true);
    }

}