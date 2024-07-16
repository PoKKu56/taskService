package ru.cinimex.taskservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.cinimex.taskservice.dto.CreateTaskRequest;
import ru.cinimex.taskservice.dto.CreateTaskResponse;
import ru.cinimex.taskservice.dto.GetTasksRequest;
import ru.cinimex.taskservice.dto.PutTaskRequest;

import java.util.UUID;

@RequestMapping("/tasks")
@RestController
public interface TaskController {

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping
    CreateTaskResponse createTask(@RequestBody CreateTaskRequest createTaskRequest);

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    ResponseEntity<?> getAllTasks(@RequestBody GetTasksRequest getTasksRequest);

    @GetMapping("/{id}")
    ResponseEntity<?> getTaskById(@PathVariable("id") UUID id);

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteTaskById(@PathVariable("id") UUID id);

    @PutMapping("/{id}")
    ResponseEntity<?> updateTaskById(@PathVariable("id") UUID id,@RequestBody PutTaskRequest putTaskRequest);
}
