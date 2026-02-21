package com.task.taskreminder.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.task.taskreminder.model.Task;
import com.task.taskreminder.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUser(User user);
    Page<Task> findByUser(User user, Pageable pageable);
    List<Task> findByDateBetween(LocalDate today, LocalDate tomorrow);
    List<Task> findByStatusNotAndDateLessThanEqual(String status, LocalDate date);

    List<Task> findByStatus(String status);



}
