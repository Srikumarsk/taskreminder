package com.task.taskreminder.service;

import com.task.taskreminder.model.Task;
import com.task.taskreminder.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository repo;

    public TaskService(TaskRepository repo) {
        this.repo = repo;
    }

    public List<Task> getAllTasks() {
        return repo.findAll();
    }

    public void saveTask(Task task) {
        repo.save(task);
    }
}
