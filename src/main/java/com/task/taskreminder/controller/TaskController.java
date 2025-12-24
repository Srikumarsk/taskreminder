package com.task.taskreminder.controller;

import com.task.taskreminder.model.Task;
import com.task.taskreminder.repository.TaskRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TaskController {

    private final TaskRepository repository;

    public TaskController(TaskRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("task", new Task());
        model.addAttribute("tasks", repository.findAll());
        return "index";
    }

    @PostMapping("/addTask")
    public String addTask(@ModelAttribute Task task) {
        repository.save(task);
        return "redirect:/";
    }
    @GetMapping("/edit/{id}")
public String editTask(@PathVariable Long id, Model model) {
    Task task = repository.findById(id).orElse(null);

    model.addAttribute("task", task);
    model.addAttribute("tasks", repository.findAll());

    return "index";
}

    // Delete Task
    @GetMapping("/delete/{id}")
public String deleteTask(@PathVariable Long id) {
    System.out.println("Deleting id = " + id);
    repository.deleteById(id);
    return "redirect:/";
}

    // Toggle Status
    @GetMapping("/toggle/{id}")
    public String toggleStatus(@PathVariable Long id) {
        Task task = repository.findById(id).orElseThrow();
        if ("DONE".equalsIgnoreCase(task.getStatus())) {
            task.setStatus("PENDING");
        } else {
            task.setStatus("DONE");
        }
        repository.save(task);
        return "redirect:/";
    }
    @GetMapping("/view/{id}")
public String viewTask(@PathVariable Long id, Model model) {
    Task task = repository.findById(id).orElse(null);
    model.addAttribute("task", task);
    return "view-task";
}

}
