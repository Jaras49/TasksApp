package com.crud.tasks.scheduler;

import com.crud.tasks.config.AdminConfig;
import com.crud.tasks.domain.mail.Mail;
import com.crud.tasks.domain.task.Task;
import com.crud.tasks.repository.TaskRepository;
import com.crud.tasks.service.SimpleEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmailScheduler {

    private static final String SUBJECT = "Tasks: Daily information";
    private static final String WELCOME_MESSAGE = "Hello this is your daily information service !";

    @Autowired
    private SimpleEmailService simpleEmailService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private AdminConfig adminConfig;

    @Scheduled(cron = "0 0 8 * * *")
    public void sentInformationEmail() {
        List<Task> tasks = taskRepository.findAll();

        simpleEmailService.sendMail(new Mail(adminConfig.getAdminMail(), SUBJECT, WELCOME_MESSAGE), tasks);
    }
}
