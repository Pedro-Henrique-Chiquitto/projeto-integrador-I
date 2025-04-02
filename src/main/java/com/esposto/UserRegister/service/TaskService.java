package com.esposto.UserRegister.service;

import com.esposto.UserRegister.model.Task;
import com.esposto.UserRegister.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public List<Task> findTasksByDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();
        return taskRepository.findByDateRange(startOfDay, endOfDay);
    }

    public Optional<Task> findTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Task updateTaskById(Task task, Long id) {
        return taskRepository.findById(id)
                .map(taskToUpdate -> {
                    taskToUpdate.setTitle(task.getTitle());
                    taskToUpdate.setDescription(task.getDescription());
                    taskToUpdate.setBeginLine(task.getBeginLine());
                    taskToUpdate.setDeadLine(task.getDeadLine());
                    return taskRepository.save(taskToUpdate);
                })
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada com o ID: " + id));
    }

    public void deleteById(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Tarefa não encontrada com o ID: " + id);
        }
        taskRepository.deleteById(id);
    }

    public List<Task> listAllTasks() {
        return taskRepository.findAll();
    }
}