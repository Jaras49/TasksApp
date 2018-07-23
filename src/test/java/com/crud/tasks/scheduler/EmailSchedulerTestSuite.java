package com.crud.tasks.scheduler;

import com.crud.tasks.config.AdminConfig;
import com.crud.tasks.domain.mail.Mail;
import com.crud.tasks.domain.task.Task;
import com.crud.tasks.repository.TaskRepository;
import com.crud.tasks.service.SimpleEmailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class EmailSchedulerTestSuite {

    private static final String SUBJECT = "Tasks: Daily information";
    private static final String WELCOME_MESSAGE = "Hello this is your daily information service !";

    @InjectMocks
    private EmailScheduler emailScheduler;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private SimpleEmailService simpleEmailService;

    @Mock
    private AdminConfig adminConfig;

    @Test
    public void shouldSendInformationMail() {

        //Given
        Task task = new Task(1L, "test", "content");
        Task task1 = new Task(2L, "title", "testContent");

        when(taskRepository.findAll()).thenReturn(Arrays.asList(task, task1));
        when(adminConfig.getAdminMail()).thenReturn("mail@gmail.com");

        //When
        emailScheduler.sentInformationEmail();

        //Then
        verify(simpleEmailService, times(1)).sendMail(
                new Mail("mail@gmail.com", SUBJECT, WELCOME_MESSAGE), Arrays.asList(task, task1));
    }
}