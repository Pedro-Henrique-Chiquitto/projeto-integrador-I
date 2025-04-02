package com.esposto.UserRegister.controllers;

import com.esposto.UserRegister.model.Task;
import com.esposto.UserRegister.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/agenda/tasks")
public class TaskController {

    @Autowired
    private TaskService tasksService;

    @GetMapping
    public String getAllTasks(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model) {

        LocalDate currentDate = date != null ? date : LocalDate.now();
        List<Task> tasks = tasksService.findTasksByDate(currentDate);

        model.addAttribute("tasks", tasks);
        model.addAttribute("currentDate", currentDate);

        return "pages/agenda";
    }

    @GetMapping("/new")
    public String createTaskForm(Model model) {
        Task newTask = new Task();
        // Define a data/hora padrão (opcional)
        newTask.setBeginLine(LocalDateTime.now());
        newTask.setDeadLine(LocalDateTime.now().plusHours(1));

        model.addAttribute("task", newTask);
        return "pages/taskForm";
    }

    @GetMapping("/{id}")
    public String getTaskById(@PathVariable Long id, Model model) {
        Task task = tasksService.findTaskById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));
        model.addAttribute("task", task);
        return "pages/taskDetail";
    }

    @GetMapping("/{id}/edit")
    public String editTaskForm(@PathVariable Long id, Model model) {
        Task task = tasksService.findTaskById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));
        model.addAttribute("task", task);
        return "pages/taskForm";
    }

    @PostMapping
    public String createTask(
            @Valid @ModelAttribute("task") Task task,
            BindingResult result,
            Model model) {

        validateTaskDates(task, result);

        if (result.hasErrors()) {
            return "pages/taskForm";
        }

        Task savedTask = tasksService.createTask(task);
        return "redirect:/agenda/tasks?success=true";
    }

    @PostMapping("/{id}")
    public String updateTask(
            @PathVariable Long id,
            @Valid @ModelAttribute("task") Task task,
            BindingResult result,
            Model model) {

        validateTaskDates(task, result);

        if (result.hasErrors()) {
            return "pages/taskForm";
        }

        task.setId(id);
        Task updatedTask = tasksService.updateTaskById(task, id);
        return "redirect:/agenda/tasks?success=true";
    }

    @GetMapping("/{id}/delete")
    public String deleteTask(@PathVariable Long id) {
        tasksService.deleteById(id);
        return "redirect:/agenda/tasks?deleted=true";
    }

    private void validateTaskDates(Task task, BindingResult result) {
        if (task.getBeginLine() != null && task.getDeadLine() != null
                && !task.getDeadLine().isAfter(task.getBeginLine())) {
            result.rejectValue("deadLine", "invalid.deadLine",
                    "O prazo final deve ser após a data de início");
        }
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDateTime.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(text == null || text.isEmpty() ? null : LocalDateTime.parse(text));
            }
        });
    }
}