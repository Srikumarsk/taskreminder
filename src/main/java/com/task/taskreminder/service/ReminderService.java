package com.task.taskreminder.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.task.taskreminder.model.Task;
import com.task.taskreminder.repository.TaskRepository;

@Service
public class ReminderService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private EmailService emailService;

    @Scheduled(cron = "0 0 9 * * ?") // every day at 9 AM
    public void sendDueDateReminders() {

        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        List<Task> tasks =
                taskRepository.findByDateBetween(today, tomorrow);

        for (Task task : tasks) {
            emailService.sendTaskReminder(
                    task.getUser().getEmail(),
                    task
            );
        }
    }
}
