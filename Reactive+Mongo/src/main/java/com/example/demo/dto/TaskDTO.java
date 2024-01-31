package com.example.demo.dto;

import com.example.demo.enums.TaskStatus;
import lombok.Data;

import java.util.Set;

@Data
public class TaskDTO {

    private String name;
    private String description;
    private TaskStatus taskStatus;
    private String authorId;
    private String assigneeId;
    private Set<String> observes;

}
