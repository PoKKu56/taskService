package ru.cinimex.taskservice.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.cinimex.taskservice.domain.TaskEntity;
import ru.cinimex.taskservice.domain.TaskEntity_;
import ru.cinimex.taskservice.dto.*;
import ru.cinimex.taskservice.exception.AuntethicationException;
import ru.cinimex.taskservice.exception.DateException;
import ru.cinimex.taskservice.exception.TaskException;
import ru.cinimex.taskservice.exception.UnknownTaskException;
import ru.cinimex.taskservice.mapper.TaskMapper;
import ru.cinimex.taskservice.repository.TaskRepository;
import java.time.OffsetDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public static Specification<TaskEntity> titleAndStatusAndNotificateAtStartAndNotificateAtEnd(
            final GetTasksRequest getTasksRequest){
        return new Specification<>(){

            @Override
            public Predicate toPredicate(Root<TaskEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

                Predicate titlePredicate = criteriaBuilder.like(root.get(TaskEntity_.TITLE), "%" +
                        getTasksRequest.getTitle() + "%");
                Predicate statusPredicate = criteriaBuilder.like(root.get(TaskEntity_.STATUS), "%" +
                        getTasksRequest.getStatus() + "%");
                Predicate notificateAtStartPredicate = criteriaBuilder.greaterThanOrEqualTo(
                        root.get(TaskEntity_.NOTIFICATE_AT), getTasksRequest.getNotificateAtStart());
                Predicate notificateAtEndPredicate = criteriaBuilder.lessThanOrEqualTo(
                        root.get(TaskEntity_.NOTIFICATE_AT), getTasksRequest.getNotificateAtEnd());
                Predicate assigneePredicate = criteriaBuilder.equal(root.get(TaskEntity_.ASSIGNEE),
                        SecurityContextHolder.getContext().getAuthentication().getName());

                return criteriaBuilder.and(titlePredicate, statusPredicate,
                        notificateAtStartPredicate, notificateAtEndPredicate, assigneePredicate);
            }
        };
    }

    public CreateTaskResponse createTask(CreateTaskRequest createTaskRequest) {

        if (createTaskRequest.getNotificateAt().isBefore(OffsetDateTime.now())){
            throw new DateException("Неверная дата срока выполнения.");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (username == null){
            throw new AuntethicationException("Something went wrong with authentication.");
        }
        TaskEntity taskEntity = taskMapper.taskDtoToEntity(createTaskRequest);
        taskEntity.setAssignee(username);
        taskRepository.save(taskEntity);

        return new CreateTaskResponse(taskEntity.getId());
    }

    public ResponseEntity<?> getTasksOfCurrentUser(GetTasksRequest getTasksRequest) {

            Specification<TaskEntity> specification = Specification.where(
                titleAndStatusAndNotificateAtStartAndNotificateAtEnd(
                getTasksRequest));

            Optional<List<TaskEntity>> tasks = taskRepository.findAll(specification);

            if (tasks.isPresent()){
                return ResponseEntity.status(HttpStatusCode.valueOf(200))
                        .body(tasks.get().stream().map(taskEntity ->
                                new GetTaskResponse(
                                        taskEntity.getId(),
                                        taskEntity.getTitle(),
                                        taskEntity.getDescription(),
                                        taskEntity.getNotificateAt()
                                )));
            }
            return ResponseEntity.status(HttpStatusCode.valueOf(500)).body("No Tasks Found");
        }

    public ResponseEntity<?> getTaskById(UUID taskId) {

        Optional<TaskEntity> task = taskRepository.findById(taskId);
        if (task.isEmpty()) {
            throw new UnknownTaskException("Unknown Task");
        }
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(new GetTaskResponse(
                task.get().getId(),
                task.get().getTitle(),
                task.get().getDescription(),
                task.get().getNotificateAt()
        ));
    }

    public ResponseEntity<?> updateTask(UUID taskId, PutTaskRequest putTaskRequest) {
        Optional<TaskEntity> task = taskRepository.findById(taskId);

        checkTaskBeforeUpdateOrDelete(task);

        taskMapper.updateTaskDtoToEntity(task.get(), putTaskRequest);

        taskRepository.save(task.get());

        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body("Выполнено");
    }

    public ResponseEntity<?> deleteTask(UUID taskId) {

        Optional<TaskEntity> task = taskRepository.findById(taskId);

        checkTaskBeforeUpdateOrDelete(task);

        taskRepository.delete(task.get());

        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body("Запись успешна удалена");
    }

    private void checkTaskBeforeUpdateOrDelete(Optional<TaskEntity> task) {

        if (task.isEmpty()){
            throw new UnknownTaskException("Unknown Task");
        }

        if (!task.get().getStatus().equals("CREATED")){
            throw new TaskException("This task is already in progress");
        }
        if (!task.get().getAssignee().equals(SecurityContextHolder.getContext().getAuthentication().getName())){
            throw new TaskException("This task isn't yours");
        }

    }


}
