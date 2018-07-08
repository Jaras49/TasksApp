package com.crud.tasks.mapper;

import com.crud.tasks.domain.task.Task;
import com.crud.tasks.domain.task.TaskDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TaskMapperTestSuite {

    @Autowired
    private TaskMapper taskMapper;

    @Test
    public void shouldMapToTask() {

        //Given
        TaskDto taskDto = new TaskDto(1L, "testTask", "testContent");

        //When

        Task task = taskMapper.mapToTask(taskDto);

        //Then
        assertEquals(1L, task.getId().longValue());
        assertEquals("testTask", task.getTitle());
        assertEquals("testContent", task.getContent());
    }

    @Test
    public void shouldMapToTaskDto() {

        //Given
        Task task = new Task(1L, "test", "content");

        //When
        TaskDto taskDto = taskMapper.mapToTaskDto(task);

        //Then
        assertEquals(1L, taskDto.getId().longValue());
        assertEquals("test", taskDto.getTitle());
        assertEquals("content", taskDto.getContent());
    }

    @Test
    public void shouldMapToTaskDtoList() {

        //Given
        Task task = new Task(1L, "test", "content");
        Task task1 = new Task(2L, "test", "contentTest");

        //When
        List<TaskDto> taskDtos = taskMapper.mapToTaskDtoList(Arrays.asList(task, task1));

        //Then
        assertEquals(1L, taskDtos.get(0).getId().longValue());
        assertEquals("test", taskDtos.get(0).getTitle());
        assertEquals("content", taskDtos.get(0).getContent());
        assertEquals(2, taskDtos.size());
    }
}