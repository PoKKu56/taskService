package ru.cinimex.taskservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.cinimex.taskservice.dto.CreateTaskRequest;
import ru.cinimex.taskservice.dto.GetTasksRequest;
import ru.cinimex.taskservice.service.TaskService;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class TaskControllerImpl implements TaskController {

    private final TaskService taskService;

    @Override
    public ResponseEntity<?> createTask(CreateTaskRequest createTaskRequest, String token) {
        return taskService.createTask(createTaskRequest, token);
    }

    @Override
    public ResponseEntity<?> getAllTasks(GetTasksRequest getTasksRequest, String token) {
        return taskService.getTasksOfCurrentUser(getTasksRequest, token);
    }

    @Override
    public ResponseEntity<?> getTaskById(UUID id, String token) {
        return taskService.getTaskById(id, token);
    }

    @Override
    public ResponseEntity<?> deleteTaskById(UUID id, String token) {
        return null;
    }
}
