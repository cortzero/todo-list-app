package com.cortzero.todoapi.repositories;

import com.cortzero.todoapi.entities.ToDo;
import com.cortzero.todoapi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ToDoRepository extends JpaRepository<ToDo, Long> {

    Optional<ToDo> findByUserAndId(User user, Long id);
    List<ToDo> findByUser(User user);

    @Modifying
    @Query("delete from ToDo t where t.user = ?1 and id = ?2")
    void deleteByUserAndId(User user, Long id);

}
