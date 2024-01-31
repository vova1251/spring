package com.example.demo.entity;

import com.example.demo.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "task")
public class TaskEntity {

    @Id
    private String id;
    private String name;
    private String description;
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();
    private TaskStatus taskStatus;
    private String authorId;
    private String assigneeId;
    @Field("observerlist")
    private Set<String> observerIds;

    @ReadOnlyProperty
    private UserEntity author;
    @ReadOnlyProperty
    private UserEntity assignee;
    @ReadOnlyProperty
    private Set<UserEntity> observers;

}
