package ru.cinimex.taskservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetTaskResponse {

    private UUID id;
    private String title;
    private String description;
    private Date notificateAt;

}
