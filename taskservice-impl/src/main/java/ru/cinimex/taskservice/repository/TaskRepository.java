package ru.cinimex.taskservice.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.cinimex.taskservice.domain.TaskEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, UUID> {

    Optional<List<TaskEntity>> findAll(Specification<TaskEntity> spec);

}
