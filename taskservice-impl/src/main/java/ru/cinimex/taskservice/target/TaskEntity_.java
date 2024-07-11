package ru.cinimex.taskservice.target;

import jakarta.persistence.metamodel.SetAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import ru.cinimex.taskservice.domain.TaskEntity;
import ru.cinimex.taskservice.domain.UserEntity;

import javax.annotation.processing.Generated;
import java.util.Date;
import java.util.UUID;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TaskEntity.class)
public class TaskEntity_ {

    public static volatile SingularAttribute<TaskEntity, UUID> id;
    public static volatile SingularAttribute<TaskEntity, String> title;
    public static volatile SingularAttribute<TaskEntity, String> description;
    public static volatile SetAttribute<TaskEntity, UserEntity> user;
    public static volatile SingularAttribute<TaskEntity, String> status;
    public static volatile SingularAttribute<TaskEntity, Date> createdAt;
    public static volatile SingularAttribute<TaskEntity, Date> updatedAt;
    public static volatile SingularAttribute<TaskEntity, Date> notificateAt;

    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String USER = "user";
    public static final String STATUS = "status";
    public static final String CREATED_AT = "createdAt";
    public static final String UPDATED_AT = "updatedAt";
    public static final String NOTIFICATE_AT = "notificateAt";
}
