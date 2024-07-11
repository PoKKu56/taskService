package ru.cinimex.taskservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateTaskRequest {

    private String title;
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date notificateAt;

}
