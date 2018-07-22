package com.crud.tasks.service;

import com.crud.tasks.config.AdminConfig;
import com.crud.tasks.domain.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.List;

@Service
public class MailCreatorService {

    private static final String TASKS_NUMBER = "tasks_number";
    @Autowired
    @Qualifier("templateEngine")
    private TemplateEngine templateEngine;

    @Autowired
    private AdminConfig adminConfig;

    public String buildTrelloCardEmail(String message) {

        List<String> functionality = new ArrayList<>();
        functionality.add("You can manage your tasks");
        functionality.add("Provides connection with Trello Account");
        functionality.add("Application allows sending tasks to Trello");

        Context context = new Context();
        context.setVariable("message", message);
        context.setVariable("admin_config", adminConfig);
        context.setVariable("button", "Visit website");
        context.setVariable("bye_message", "Kind Regards");
        context.setVariable("companyDetails", adminConfig.getAppName() + ": " + adminConfig.getAppEmail());
        context.setVariable("preview", message.substring(0, 30));
        context.setVariable("show_button", true);
        context.setVariable("is_friend", true);
        context.setVariable("application_functionality", functionality);

        return templateEngine.process("mail/created-trello-card-mail", context);
    }

    public String buildDailyEmail(String message, List<Task> tasks) {

        Context context = new Context();
        if (tasks.isEmpty()) {
            context.setVariable(TASKS_NUMBER, "You dont have any available tasks!!");
        }
        if (tasks.size() == 1) {
            context.setVariable(TASKS_NUMBER, "You have 1 available task: ");
        } else {
            context.setVariable(TASKS_NUMBER, "You have " + tasks.size() + " available tasks: ");
        }
        context.setVariable("preview", message.substring(0, 20));
        context.setVariable("welcome_message", message);
        context.setVariable("tasks", tasks);
        context.setVariable("show_button", true);
        context.setVariable("is_friend", true);
        context.setVariable("button", "Visit website");
        context.setVariable("bye_message", "Kind Regards");
        context.setVariable("companyDetails", adminConfig.getAppName() + ": " + adminConfig.getAppEmail());
        context.setVariable("admin_config", adminConfig);

        return templateEngine.process("mail/daily-mail", context);
    }
}
