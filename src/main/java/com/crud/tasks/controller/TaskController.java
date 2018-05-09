package com.crud.tasks.controller;

import com.crud.tasks.domain.TaskDto;

import java.util.ArrayList;
import java.util.List;

public class TaskController {

    public List<TaskDto> getTasks() {

        return new ArrayList<>();
    }

    public TaskDto getTask(Long taskId) {

        return new TaskDto(1L, "test title", "test_content");
    }

    public void deleteTask(Long id) {

    }

    public TaskDto updateTask(TaskDto taskDto) {

        return new TaskDto(1L, "edited test title", "test content");
    }

    public void createTask(TaskDto taskDto) {

    }
}
