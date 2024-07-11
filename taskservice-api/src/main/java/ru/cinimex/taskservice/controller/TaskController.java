package ru.cinimex.taskservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.cinimex.taskservice.dto.CreateTaskRequest;
import ru.cinimex.taskservice.dto.GetTasksRequest;

import java.util.UUID;

@RequestMapping("/tasks")
@RestController
public interface TaskController {

    @PostMapping("/{jwtToken}")
    ResponseEntity<?> createTask(@RequestBody CreateTaskRequest createTaskRequest,
                                 @PathVariable("jwtToken") String jwtToken);

    @GetMapping("/{jwtToken}")
    ResponseEntity<?> getAllTasks(@RequestBody GetTasksRequest getTasksRequest,
                                  @PathVariable("jwtToken") String jwtToken);

    @GetMapping("/{jwtToken}/{id}")
    ResponseEntity<?> getTaskById(@PathVariable("id") UUID id, @PathVariable("jwtToken") String jwtToken);

    @DeleteMapping("/{jwtToken}/{id}")
    ResponseEntity<?> deleteTaskById(@PathVariable("id") UUID id, @PathVariable("jwtToken") String jwtToken);








}
