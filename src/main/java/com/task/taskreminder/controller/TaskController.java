package com.task.taskreminder.controller;

import com.task.taskreminder.model.Task;
import com.task.taskreminder.repository.TaskRepository;
import com.task.taskreminder.model.User;


import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TaskController {

    private final TaskRepository repository;


    public TaskController(TaskRepository repository) {
        this.repository = repository;
    }
  


@GetMapping("/home")
public String home(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String priority,
        Model model ,HttpSession session) {
            User loggedUser = (User) session.getAttribute("loggedUser");

    
  Sort prioritySort = Sort.by(
        Sort.Order.asc(
                "priority"
        )
);

Pageable pageable = PageRequest.of(page, size, prioritySort);

   Page<Task> taskPage = repository.findByUser(loggedUser, pageable);

    List<Task> tasks = taskPage.getContent();
    tasks = tasks.stream()
        .sorted((t1, t2) -> {
            List<String> order = List.of("HIGH", "MEDIUM", "LOW");
            return Integer.compare(
                    order.indexOf(t1.getPriority()),
                    order.indexOf(t2.getPriority())
            );
        })
        .toList();


    // Apply filters on current page (simple approach)
    if (keyword != null && !keyword.isEmpty()) {
        tasks = tasks.stream()
                .filter(t -> t.getTitle().toLowerCase().contains(keyword.toLowerCase()))
                .toList();
    }
    if (status != null && !status.isEmpty()) {
        tasks = tasks.stream()  
                .filter(t -> t.getStatus().equalsIgnoreCase(status))
                .toList();
    }
    if (priority != null && !priority.isEmpty()) {
        tasks = tasks.stream()
                .filter(t -> t.getPriority().equalsIgnoreCase(priority))
                .toList();
    }

    // ✅ Counts (from filtered list)
    long pendingCount = tasks.stream().filter(t -> "PENDING".equalsIgnoreCase(t.getStatus())).count();
    long inProgressCount = tasks.stream().filter(t -> "IN_PROGRESS".equalsIgnoreCase(t.getStatus())).count();
    long doneCount = tasks.stream().filter(t -> "DONE".equalsIgnoreCase(t.getStatus())).count();

    model.addAttribute("pendingCount", pendingCount);
    model.addAttribute("inProgressCount", inProgressCount);
    model.addAttribute("doneCount", doneCount);

    model.addAttribute("tasks", tasks);
    model.addAttribute("task", new Task());

    // pagination data
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", taskPage.getTotalPages());
    model.addAttribute("size", size);

    return "index";
    
}
@GetMapping("/filter/search")
public String filterTasks(
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String priority,
        Model model,
        HttpSession session) {

    User loggedUser = (User) session.getAttribute("loggedUser");

    List<Task> tasks = repository.findByUser(loggedUser);

    if (keyword != null && !keyword.isEmpty()) {
        tasks = tasks.stream()
                .filter(t -> t.getTitle().toLowerCase().contains(keyword.toLowerCase()))
                .toList();
    }

    if (status != null && !status.isEmpty()) {
        tasks = tasks.stream()
                .filter(t -> t.getStatus().equalsIgnoreCase(status))
                .toList();
    }

    if (priority != null && !priority.isEmpty()) {
        tasks = tasks.stream()
                .filter(t -> t.getPriority().equalsIgnoreCase(priority))
                .toList();
    }

    model.addAttribute("tasks", tasks);
    return "filter";
}




@GetMapping("/filter")
public String filterPage(HttpSession session) {

    if (session.getAttribute("loggedUser") == null) {
        return "redirect:/login";
    }

    return "filter";
}


@GetMapping("/welcome")
public String welcome() {
    return "welcome";
}
@GetMapping("/cards")
public String cardView(Model model, HttpSession session) {

    User loggedUser = (User) session.getAttribute("loggedUser");

    List<Task> tasks = repository.findByUser(loggedUser);

    //  Priority order: HIGH → MEDIUM → LOW
    tasks = tasks.stream()
            .sorted((t1, t2) -> {
                List<String> order = List.of("HIGH", "MEDIUM", "LOW");
                return Integer.compare(
                        order.indexOf(t1.getPriority()),
                        order.indexOf(t2.getPriority())
                );
            })
            .toList();

    model.addAttribute("tasks", tasks);
    return "card-view";
}

    @PostMapping("/addTask")
public String addTask(@ModelAttribute Task task, HttpSession session) {

    User loggedUser = (User) session.getAttribute("loggedUser");

    task.setUser(loggedUser);   // 🔥 STEP 3 (IMPORTANT)

    repository.save(task);

    return "redirect:/home";
}

@GetMapping("/edit/{id}")
public String showEditPage(@PathVariable Long id, Model model) {
    Task task = repository.findById(id).orElseThrow();
    model.addAttribute("task", task);
    return "edit";   // edit.html
}


    // Delete Task
    @GetMapping("/delete/{id}")
public String deleteTask(@PathVariable Long id) {
    System.out.println("Deleting id = " + id);
    repository.deleteById(id);
    return "redirect:/home";
}

@GetMapping("/toggle/{id}")
public String toggleStatus(@PathVariable Long id) {
    Task task = repository.findById(id).orElse(null);

    if (task != null) {
        if ("DONE".equalsIgnoreCase(task.getStatus())) {
            task.setStatus("PENDING");
        } else {
            task.setStatus("DONE");
            repository.save(task);
            return "redirect:/home?done=true"; // 👈 important
        }
        repository.save(task);
    }
    return "redirect:/home";
}




    @GetMapping("/view/{id}")
public String viewTask(@PathVariable Long id, Model model) {
    Task task = repository.findById(id).orElse(null);
    model.addAttribute("task", task);
    return "view-task";
}
@GetMapping("/search")
public String search(@RequestParam("q") String q, Model model) {
    model.addAttribute("task", new Task());
    model.addAttribute("tasks",
            repository.findAll().stream()
                    .filter(t -> t.getTitle().toLowerCase().contains(q.toLowerCase()))
                    .toList()
    );
    return "index";
}
@GetMapping("/api/tasks")
@ResponseBody
public List<Task> getTasks(HttpSession session) {

    User loggedUser = (User) session.getAttribute("loggedUser");

    return repository.findByUser(loggedUser);
}


@GetMapping("/calendar")
public String calendarView() {
    return "calendar";
}
@PostMapping("/updateTask")
public String updateTask(@ModelAttribute Task task, HttpSession session) {

    User loggedUser = (User) session.getAttribute("loggedUser");
    task.setUser(loggedUser);   //    keep ownership

    repository.save(task);
    return "redirect:/home";
}




}
