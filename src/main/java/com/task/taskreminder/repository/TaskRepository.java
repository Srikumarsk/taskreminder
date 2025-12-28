package com.task.taskreminder.repository;

import com.task.taskreminder.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
   
}
