package com.esposto.UserRegister.controllers;

import com.esposto.UserRegister.model.Task;
import com.esposto.UserRegister.repositories.TaskRepository;
import com.esposto.UserRegister.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
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
            Model model,
            Authentication authentication) {

        LocalDate currentDate = date != null ? date : LocalDate.now();

        // Modificado para buscar TODAS as tarefas para a data especificada
        LocalDateTime startOfDay = currentDate.atStartOfDay();
        LocalDateTime endOfDay = currentDate.plusDays(1).atStartOfDay();
        List<Task> tasks = tasksService.findTasksByDate(currentDate);  // Use o serviço em vez do repositório

        // Adiciona o email do usuário atual ao modelo para verificações na view
        model.addAttribute("currentUserEmail", authentication.getName());
        model.addAttribute("tasks", tasks);
        model.addAttribute("currentDate", currentDate);

        return "pages/taskList";
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
    public String getTaskById(@PathVariable Long id, Model model, Authentication authentication) {
        // Em vez de verificar se a tarefa pertence ao usuário atual,
        // simplesmente busque a tarefa pelo ID
        Task task = tasksService.findTaskById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        // Adicione o email do usuário atual ao modelo para verificações na view
        model.addAttribute("currentUserEmail", authentication.getName());
        model.addAttribute("task", task);
        return "pages/taskDetail";
    }


    @GetMapping("/{id}/edit")
    public String editTaskForm(@PathVariable Long id, Model model, Authentication authentication) {
        String username = authentication.getName();
        Task task = tasksService.findTaskByIdAndUser(id, username)  // Crie este método no service
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada ou acesso não autorizado"));

        model.addAttribute("task", task);
        return "pages/taskForm";
    }

    @PostMapping
    public String createTask(
            @Valid @ModelAttribute("task") Task task,
            BindingResult result,
            Model model,
            Authentication authentication) {  // Adicione o parâmetro Authentication

        validateTaskDates(task, result);

        if (result.hasErrors()) {
            return "pages/taskForm";
        }

        // Associe o usuário logado à tarefa
        String username = authentication.getName();
        tasksService.createTask(task, username);  // Modifique o service para aceitar o username

        return "redirect:/agenda/tasks?success=true";
    }
    @PostMapping("/{id}")
    public String updateTask(
            @PathVariable Long id,
            @Valid @ModelAttribute("task") Task task,
            BindingResult result,
            Model model,
            Authentication authentication) {  // Adicione o parâmetro Authentication

        validateTaskDates(task, result);

        if (result.hasErrors()) {
            return "pages/taskForm";
        }

        // Associe o usuário logado à tarefa
        String username = authentication.getName();
        task.setId(id);
        tasksService.updateTaskById(task, id, username);  // Modifique o service para aceitar o username

        return "redirect:/agenda/tasks?success=true";
    }

    @GetMapping("/{id}/delete")
    public String deleteTask(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        tasksService.deleteByIdAndUser(id, username);  // Modifique o service para aceitar o username
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