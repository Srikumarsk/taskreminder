package com.task.taskreminder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;



@SpringBootApplication
@EnableScheduling
public class TaskreminderApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskreminderApplication.class, args);
    }
    @Bean
public CommandLineRunner checkScheduler(com.task.taskreminder.scheduler.ReminderScheduler scheduler) {
    return args -> System.out.println("Scheduler Bean Injected Successfully");
}
}