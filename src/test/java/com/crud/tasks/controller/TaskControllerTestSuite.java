package com.crud.tasks.controller;

import com.crud.tasks.domain.task.Task;
import com.crud.tasks.domain.task.TaskDto;
import com.crud.tasks.mapper.TaskMapper;
import com.crud.tasks.service.DbService;
import com.google.gson.Gson;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
@RunWith(SpringRunner.class)
public class TaskControllerTestSuite {

    private static final String TASKS_URL = "/v1/tasks";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DbService dbService;

    @MockBean
    private TaskMapper taskMapper;

    @Test
    public void shouldFetchTasks() throws Exception {

        //Given
        Task task = new Task(1L, "test task", "description");
        TaskDto taskDto = new TaskDto(1L, "test task", "description");

        when(dbService.getAllTasks()).thenReturn(Collections.singletonList(task));
        when(taskMapper.mapToTaskDtoList(Collections.singletonList(task))).thenReturn(Collections.singletonList(taskDto));

        //When & Then
        mockMvc.perform(get(TASKS_URL).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("test task")))
                .andExpect(jsonPath("$[0].content", is("description")));
    }

    @Test
    public void shouldFetchTask() throws Exception {

        //Given
        Optional<Task> task = Optional.of(new Task(1L, "test", "content"));
        TaskDto taskDto = new TaskDto(1L, "test", "content");

        String taskId = "1";

        when(dbService.getTask(1L)).thenReturn(task);
        when(taskMapper.mapToTaskDto(task.get())).thenReturn(taskDto);

        //when & Then
        mockMvc.perform(get(TASKS_URL.concat("/" + taskId))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("test")))
                .andExpect(jsonPath("$.content", is("content")));
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Test
    public void shouldThrowTaskNotFoundException() throws Exception {

        //Given
        Optional<Task> task = Optional.empty();

        String taskId = "1";

        when(dbService.getTask(1L)).thenReturn(task);

        //When & Then
        expectedException.expectCause(isA(TaskNotFoundException.class));

        mockMvc.perform(get(TASKS_URL.concat("/" + taskId))
                .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void shouldDeleteTask() throws Exception {

        //Given
        String taskId = "1";

        //When & Then
        mockMvc.perform(delete(TASKS_URL.concat("/" + taskId))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldUpdateTask() throws Exception {

        //Given
        TaskDto taskDto = new TaskDto(1L, "test", "content");
        Task task = new Task(1L, "test", "content");

        Gson gson = new Gson();
        String jsonContent = gson.toJson(taskDto);

        when(taskMapper.mapToTask(argThat(any(TaskDto.class)))).thenReturn(task);
        when(dbService.saveTask(task)).thenReturn(task);
        when(taskMapper.mapToTaskDto(task)).thenReturn(taskDto);

        //When & Then
        mockMvc.perform(put(TASKS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("test")))
                .andExpect(jsonPath("$.content", is("content")));
    }

    @Test
    public void shouldCreateTask() throws Exception {

        //Given
        TaskDto taskDto = new TaskDto(1L, "test", "description");
        String jsonContent = new Gson().toJson(taskDto);

        when(taskMapper.mapToTask(argThat(any(TaskDto.class)))).thenReturn(new Task(1L, "test", "description"));
        //When & Then
        mockMvc.perform(post(TASKS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(status().isOk());
    }
}