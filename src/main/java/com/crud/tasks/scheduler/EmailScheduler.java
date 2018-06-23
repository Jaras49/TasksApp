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

    private static final String NO_TASKS = "You dont have any tasks";
    private static final String SUBJECT = "Tasks: Daily information";
    private static final String TASK = "task";
    private static final String TASKS = "tasks";
    private static final String IN_DATABASE = "Currently in database you got: ";
    private static final String CURRENT = "Current ";
    private static final String CONTENT = " Content: ";
    private static final String TITLE = "Title: ";
    private static final String COLON = ":";
    private static final String SEMICOLON_WITH_WHITE_SPACES = " ; ";
    private static final String WHITE_SPACE = " ";
    private static final String NEW_LINE = "\n";

    @Autowired
    private SimpleEmailService simpleEmailService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private AdminConfig adminConfig;

    @Scheduled(cron = "0 0 8 * * *")
    public void sentInformationEmail() {
        List<Task> tasks = taskRepository.findAll();
        if (!tasks.isEmpty()) {
            simpleEmailService.send(new Mail(adminConfig.getAdminMail(), SUBJECT, buildMailMessage(tasks)));
        } else {
            simpleEmailService.send(new Mail(adminConfig.getAdminMail(), SUBJECT, buildMailMessage()));
        }
    }

    private String buildMailMessage() {
        return NO_TASKS;
    }

    private String buildMailMessage(List<Task> tasks) {
        long size = taskRepository.count();

        String taskWord;
        if (tasks.size() == 1) {
            taskWord = TASK;
        } else {
            taskWord = TASKS;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(IN_DATABASE).append(size).append(WHITE_SPACE).append(taskWord).append(NEW_LINE);
        sb.append(CURRENT).append(taskWord).append(COLON).append(NEW_LINE);
        for (Task task : tasks) {
            sb.append(TITLE).append(task.getTitle()).append(SEMICOLON_WITH_WHITE_SPACES).append(CONTENT).append(task.getContent());
            sb.append(NEW_LINE);
        }
        return sb.toString();
    }
}
