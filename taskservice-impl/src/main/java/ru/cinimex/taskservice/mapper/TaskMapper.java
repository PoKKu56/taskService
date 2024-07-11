package ru.cinimex.taskservice.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.cinimex.taskservice.domain.TaskEntity;
import ru.cinimex.taskservice.dto.CreateTaskRequest;
import ru.cinimex.taskservice.service.TaskService;

import java.util.Date;

@Mapper(componentModel = "spring", uses = {TaskService.class})
public interface TaskMapper {

    CreateTaskRequest taskEntityToDto(TaskEntity taskEntity);
    TaskEntity taskDtoToEntity(CreateTaskRequest createTaskRequest);

    @AfterMapping
    default void fillAdditionalFields(@MappingTarget TaskEntity taskEntity) {
        if (taskEntity.getStatus() != null) {
            taskEntity.setStatus("CREATED");
        }
        if (taskEntity.getCreatedAt() != null){
            taskEntity.setCreatedAt(new Date());
        }
        taskEntity.setUpdatedAt(new Date());
    }

}
