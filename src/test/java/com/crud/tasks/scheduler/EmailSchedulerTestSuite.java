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

    @InjectMocks
    private EmailScheduler emailScheduler;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private SimpleEmailService simpleEmailService;

    @Mock
    private AdminConfig adminConfig;

    @Test
    public void shouldSendInformationMailWhenThereAreTasks() {

        //Given
        Task task = new Task(1L, "test", "content");
        Task task1 = new Task(2L, "title", "testContent");

        when(taskRepository.findAll()).thenReturn(Arrays.asList(task, task1));
        when(adminConfig.getAdminMail()).thenReturn("mail@gmail.com");

        String message = "Currently in database you got: 2 tasks\n" +
                "Current tasks:\n" +
                "Title: test ;  Content: content\n" +
                "Title: title ;  Content: testContent\n";

        //When
        emailScheduler.sentInformationEmail();

        //Then
        verify(simpleEmailService, times(1)).send(new Mail("mail@gmail.com", SUBJECT, message));
    }

    @Test
    public void shouldSentInformationMailWhenThereIsOneTask() {

        //Given
        Task task = new Task(1L, "test", "content");

        when(taskRepository.findAll()).thenReturn(Collections.singletonList(task));
        when(adminConfig.getAdminMail()).thenReturn("mail@gmail.com");

        String message = "Currently in database you got: 1 task\n" +
                "Current task:\n" +
                "Title: test ;  Content: content\n";

        //When
        emailScheduler.sentInformationEmail();

        //Then
        verify(simpleEmailService, times(1)).send(new Mail("mail@gmail.com", SUBJECT, message));
    }
    @Test
    public void shouldSendInformationMailWhenThereAreNoTasks() {

        //Given
        when(taskRepository.findAll()).thenReturn(Collections.emptyList());
        when(adminConfig.getAdminMail()).thenReturn("mail@gmail.com");

        String message = "You dont have any tasks";

        //when
        emailScheduler.sentInformationEmail();

        //Then
        verify(simpleEmailService, times(1)).send(new Mail("mail@gmail.com", SUBJECT, message));
    }
}