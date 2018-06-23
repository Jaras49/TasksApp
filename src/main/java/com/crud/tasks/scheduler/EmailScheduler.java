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
    @Autowired
    private SimpleEmailService simpleEmailService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private AdminConfig adminConfig;
//cron = "0 0 10 * * *"
    @Scheduled(fixedDelay = 10000)
    public void sentInformationEmail() {
        simpleEmailService.send(new Mail(adminConfig.getAdminMail(), SUBJECT, buildMailMessage()));
    }

    private String buildMailMessage() {
        long size = taskRepository.count();
        List<Task> all = taskRepository.findAll();
        StringBuilder sb = new StringBuilder();
        sb.append("Currently in database you got: ");
        sb.append(size);
        sb.append(" tasks\n");
        sb.append("Current tasks: \n");
        for (Task task : all) {
            sb.append("Title: ");
            sb.append(task.getTitle());
            sb.append(" ; ");
            sb.append(" Content: ");
            sb.append(task.getContent());
            sb.append("\n");
        }
        return sb.toString();
    }
}
