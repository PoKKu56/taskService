package ru.cinimex.taskservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.cinimex.taskservice.dto.CreateTaskRequest;
import ru.cinimex.taskservice.dto.CreateTaskResponse;
import ru.cinimex.taskservice.dto.GetTasksRequest;
import ru.cinimex.taskservice.dto.PutTaskRequest;
import ru.cinimex.taskservice.service.TaskService;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class TaskControllerImpl implements TaskController {

    private final TaskService taskService;

    @Override
    public CreateTaskResponse createTask(CreateTaskRequest createTaskRequest) {
        return taskService.createTask(createTaskRequest);
    }

    @Override
    public ResponseEntity<?> getAllTasks(GetTasksRequest getTasksRequest) {
        return taskService.getTasksOfCurrentUser(getTasksRequest);
    }

    @Override
    public ResponseEntity<?> getTaskById(UUID id) {
        return taskService.getTaskById(id);
    }

    @Override
    public ResponseEntity<?> deleteTaskById(UUID id) {
        return taskService.deleteTask(id);
    }

    @Override
    public ResponseEntity<?> updateTaskById(UUID id, PutTaskRequest putTaskRequest) {
        return taskService.updateTask(id, putTaskRequest);
    }
}
