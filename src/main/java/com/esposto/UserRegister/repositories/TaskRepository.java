package com.esposto.UserRegister.repositories;

import com.esposto.UserRegister.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // Consulta por intervalo de datas (início ou término)
    @Query("SELECT t FROM Task t WHERE " +
            "(t.beginLine BETWEEN :start AND :end) OR " +
            "(t.deadLine BETWEEN :start AND :end)")
    List<Task> findByDateRange(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    // Consulta por intervalo de datas e usuário (Derived Query)
    List<Task> findByBeginLineBetweenAndUser_Email(
            LocalDateTime start,
            LocalDateTime end,
            String email
    );

    // Consulta por intervalo de datas e usuário (JPQL explícita)
    @Query("SELECT t FROM Task t WHERE " +
            "t.beginLine BETWEEN :start AND :end AND " +
            "t.user.email = :email")
    List<Task> findByDateRangeAndUser(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("email") String email
    );

    // Consulta por ID e usuário (para segurança)
    Optional<Task> findByIdAndUser_Email(Long id, String email);

    // Listar todas as tarefas de um usuário
    List<Task> findByUser_Email(String email);

    // Consulta para tarefas atrasadas de um usuário
    @Query("SELECT t FROM Task t WHERE " +
            "t.deadLine < CURRENT_TIMESTAMP AND " +
            "t.user.email = :email")
    List<Task> findOverdueTasksByUser(@Param("email") String email);
}