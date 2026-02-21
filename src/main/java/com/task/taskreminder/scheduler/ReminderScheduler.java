package com.task.taskreminder.scheduler;

import com.task.taskreminder.model.Task;
import com.task.taskreminder.repository.TaskRepository;
import com.task.taskreminder.service.EmailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ReminderScheduler {

    private final TaskRepository taskRepository;
    private final EmailService emailService;

    public ReminderScheduler(TaskRepository taskRepository,
                             EmailService emailService) {
        this.taskRepository = taskRepository;
        this.emailService = emailService;
        System.out.println("✅ ReminderScheduler Bean Created");
    }

    // 🔥 Runs every MORNING AT 8AM
    @Scheduled(cron = "0 0 8 * * ?")
    public void sendIntervalReminders() {

        System.out.println("⏰ Checking reminders...");

        List<Task> tasks = taskRepository.findByStatus("PENDING");

        for (Task task : tasks) {

    if (task.getUser() == null) {
        System.out.println("⚠ Task has no user. Skipping: " + task.getTitle());
        continue;
    }

    Integer interval = task.getReminderInterval();
    if (interval == null || interval <= 0) continue;

    LocalDateTime now = LocalDateTime.now();
    LocalDateTime lastSent = task.getLastReminderSentAt();

    boolean shouldSend = false;

    if (lastSent == null) {
        shouldSend = true;
    } else if (lastSent.plusMinutes(interval).isBefore(now)) {
        shouldSend = true;
    }

    if (shouldSend) {

        emailService.sendTaskReminder(
                task.getUser().getEmail(),
                task
        );

        task.setLastReminderSentAt(now);
        taskRepository.save(task);

        System.out.println("📩 Reminder sent for task: " + task.getTitle());
    }
}
    }
}