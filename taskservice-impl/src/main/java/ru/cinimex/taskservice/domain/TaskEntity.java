package ru.cinimex.taskservice.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "task")
@Getter
@Setter
@NoArgsConstructor
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;

    private String description;

    private String status;

    private Date createdAt;

    private Date updatedAt;

    private Date notificateAt;

    @ManyToOne
    @JoinColumn(name="assignee_id", referencedColumnName = "id")
    private UserEntity user;

}
