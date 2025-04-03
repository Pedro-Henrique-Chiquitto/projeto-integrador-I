package com.esposto.UserRegister.service;

import com.esposto.UserRegister.model.Task;
import com.esposto.UserRegister.model.AppUser;
import com.esposto.UserRegister.repositories.TaskRepository;
import com.esposto.UserRegister.repositories.AppUserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final AppUserRepository userRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, AppUserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Task createTask(Task task, String username) {
        AppUser user = userRepository.findUserByEmail(username);
        if (user == null) {
            throw new RuntimeException("Usuário não encontrado");
        }

        // Definir o nome do criador
        task.setCreatorName(user.getFirstName() + " " + user.getLastName());

        // Associar a tarefa ao usuário
        task.setUser(user);

        return taskRepository.save(task);
    }
    public List<Task> findTasksByDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();
        return taskRepository.findByDateRange(startOfDay, endOfDay);
    }

    public List<Task> findTasksByDateAndUser(LocalDate date, String email) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();
        return taskRepository.findByBeginLineBetweenAndUser_Email(start, end, email);
    }

    public Optional<Task> findTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Optional<Task> findTaskByIdAndUser(Long id, String email) {
        return taskRepository.findByIdAndUser_Email(id, email);
    }

    @Transactional
    public Task updateTaskById(Task task, Long id, String email) {
        return taskRepository.findByIdAndUser_Email(id, email)
                .map(existingTask -> {
                    existingTask.setTitle(task.getTitle());
                    existingTask.setDescription(task.getDescription());
                    existingTask.setBeginLine(task.getBeginLine());
                    existingTask.setDeadLine(task.getDeadLine());
                    return taskRepository.save(existingTask);
                })
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada ou acesso não autorizado"));
    }

    @Transactional
    public void deleteById(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Tarefa não encontrada com o ID: " + id);
        }
        taskRepository.deleteById(id);
    }

    @Transactional
    public void deleteByIdAndUser(Long id, String email) {
        Task task = taskRepository.findByIdAndUser_Email(id, email)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada ou acesso não autorizado"));
        taskRepository.delete(task);
    }

    public List<Task> listAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> listAllTasksByUser(String email) {
        return taskRepository.findByUser_Email(email);
    }


}