package ru.cinimex.taskservice.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.cinimex.taskservice.domain.TaskEntity;
import ru.cinimex.taskservice.domain.UserEntity;
import ru.cinimex.taskservice.dto.CreateTaskRequest;
import ru.cinimex.taskservice.dto.CreateTaskResponse;
import ru.cinimex.taskservice.dto.GetTasksRequest;
import ru.cinimex.taskservice.dto.GetTaskResponse;
import ru.cinimex.taskservice.exception.DateException;
import ru.cinimex.taskservice.mapper.TaskMapper;
import ru.cinimex.taskservice.target.TaskEntity_;
import ru.cinimex.taskservice.repository.TaskRepository;
import ru.cinimex.taskservice.repository.UserRepository;

import java.text.SimpleDateFormat;
import java.util.*;


@RequiredArgsConstructor
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static Specification<TaskEntity> titleAndStatusAndNotificateAtStartAndNotificateAtEnd(
            final GetTasksRequest getTasksRequest, final UserEntity userEntity){
        return new Specification<TaskEntity>(){

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
                Predicate user = criteriaBuilder.equal(root.get(TaskEntity_.USER), userEntity);

                return criteriaBuilder.and(titlePredicate, statusPredicate,
                        notificateAtStartPredicate, notificateAtEndPredicate, user);
            }
        };
    }

    public ResponseEntity<?> createTask(CreateTaskRequest createTaskRequest, String token) {

        if (checkToken(token)){
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("token is null");
        }

        if (createTaskRequest.getNotificateAt().before(new Date())){
            throw new DateException("Неверная дата срока выполнения.");
        }


        Optional<UserEntity> user = getUserByToken(token);

        if(user.isPresent()){

            TaskEntity taskEntity = taskMapper.taskDtoToEntity(createTaskRequest);
            taskEntity.setUser(user.get());
            taskRepository.save(taskEntity);

            return ResponseEntity.status(HttpStatusCode.valueOf(200))
                    .body(new CreateTaskResponse(taskEntity.getId()));
        }

        return ResponseEntity.status(HttpStatusCode.valueOf(500)).body("Unknown Error");
    }

    public ResponseEntity<?> getTasksOfCurrentUser(GetTasksRequest getTasksRequest, String token) {

        if (checkToken(token)){
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("token is null");
        }

        Optional<UserEntity> user = getUserByToken(token);
        if(user.isPresent()){
            Specification<TaskEntity> specification = Specification.where(
                    titleAndStatusAndNotificateAtStartAndNotificateAtEnd(
                    getTasksRequest, user.get()));

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



        return ResponseEntity.status(HttpStatusCode.valueOf(500)).body("Unknown Error");
    }

    public ResponseEntity<?> getTaskById(UUID taskId, String token){

        if (checkToken(token)){
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("token is null");
        }

        Optional<UserEntity> user = getUserByToken(token);
        if(user.isPresent()){
            Optional<TaskEntity> task = taskRepository.findById(taskId);
            if (task.isPresent()){
                return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(new GetTaskResponse(
                        task.get().getId(),
                        task.get().getTitle(),
                        task.get().getDescription(),
                        task.get().getNotificateAt()
                ));
            }
        }

        return ResponseEntity.status(HttpStatusCode.valueOf(500)).body("Unknown Error");
    }

    private Optional<UserEntity> getUserByToken(String token){
        return userRepository.findByUsername(jwtService.executeUserName(token));
    }

    private boolean checkToken(String token){
        return token == null;
    }

}
