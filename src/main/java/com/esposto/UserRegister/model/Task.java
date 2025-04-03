package com.esposto.UserRegister.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Future;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
// Remova este import
// import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user; // Alterado de User para AppUser

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O título é obrigatório")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "A descrição é obrigatória")
    @Column(nullable = false)
    private String description;

    @NotNull(message = "A data de início é obrigatória")
    @Column(nullable = false)
    private LocalDateTime beginLine;

    @NotNull(message = "O prazo final é obrigatório")
    @Future(message = "O prazo final deve ser no futuro")
    @Column(nullable = false)
    private LocalDateTime deadLine;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "creator_name")
    private String creatorName;

    // Validação customizada
    public boolean isValidDeadline() {
        return deadLine.isAfter(beginLine);
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getBeginLine() {
        return beginLine;
    }

    public void setBeginLine(LocalDateTime beginLine) {
        this.beginLine = beginLine;
    }

    public LocalDateTime getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(LocalDateTime deadLine) {
        this.deadLine = deadLine;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public AppUser getUser() { // Alterado o tipo de retorno para AppUser
        return user;
    }

    public void setUser(AppUser user) { // Alterado o tipo do parâmetro para AppUser
        this.user = user;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", beginLine=" + beginLine +
                ", deadLine=" + deadLine +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}