package com.example.demo.service;

import com.example.demo.dto.ObserveDTO;
import com.example.demo.dto.TaskDTO;
import com.example.demo.entity.TaskEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.mapper.TaskMapper;
import com.example.demo.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserService userService;

    @Autowired
    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper, UserService userService) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.userService = userService;
    }

    public Flux<TaskEntity> getAllTasks() {
        Flux<TaskEntity> taskFlux = taskRepository.findAll();
        return taskFlux.flatMap(taskEntity -> {
            Mono<UserEntity> author = userService.getUserById(taskEntity.getAuthorId());
            Mono<UserEntity> assignee = userService.getUserById(taskEntity.getAssigneeId());
            Mono<Set<UserEntity>> observes = userService.getAllUsers().filter(userEntity -> {
                if (taskEntity.getObserverIds() != null) {
                    return taskEntity.getObserverIds().contains(userEntity.getId());
                }
                return false;
            }).collect(Collectors.toSet());

            return Mono.zip(author, assignee, observes)
                    .map(t -> {
                        taskEntity.setAuthor(t.getT1());
                        taskEntity.setAssignee(t.getT2());
                        taskEntity.setObservers(t.getT3());
                        return taskEntity;
                    });
        });
    }

    public Mono<TaskEntity> getTaskById(String id) {
        Mono<TaskEntity> taskMono = taskRepository.findById(id);
        return taskMono.flatMap(taskEntity -> {
            Mono<UserEntity> author = userService.getUserById(taskEntity.getAuthorId());
            Mono<UserEntity> assignee = userService.getUserById(taskEntity.getAssigneeId());
            Mono<Set<UserEntity>> observes = userService.getAllUsers().filter(userEntity -> {
                return taskEntity.getObserverIds().contains(userEntity.getId());
            }).collect(Collectors.toSet());

            return Mono.zip(author, assignee, observes)
                    .map(t -> {
                        taskEntity.setAuthor(t.getT1());
                        taskEntity.setAssignee(t.getT2());
                        taskEntity.setObservers(t.getT3());
                        return taskEntity;
                    });
        });
    }

    public Mono<TaskEntity> addTask(TaskDTO taskDTO) {
        TaskEntity task = taskMapper.fromTaskDTO2TaskEntity(taskDTO);
        task.setId(UUID.randomUUID().toString());

        return taskRepository.save(task);
    }

    public Mono<TaskEntity> updateTaskById(String id, TaskDTO taskDTO) {
        Mono<TaskEntity> taskMono = taskRepository.findById(id);
        return taskMono.flatMap(taskEntity -> {

            taskEntity.setUpdatedAt(Instant.now());

            if (StringUtils.hasText(taskDTO.getName())) {
                taskEntity.setName(taskDTO.getName());
            }

            if (StringUtils.hasText(taskDTO.getDescription())) {
                taskEntity.setDescription(taskDTO.getDescription());
            }

            if (StringUtils.hasText(String.valueOf(taskDTO.getTaskStatus()))) {
                taskEntity.setTaskStatus(taskDTO.getTaskStatus());
            }

            if (StringUtils.hasText(taskDTO.getAuthorId())) {
                taskEntity.setAuthorId(taskDTO.getAuthorId());
            }

            if (StringUtils.hasText(taskDTO.getAssigneeId())) {
                taskEntity.setAssigneeId(taskDTO.getAssigneeId());
            }

            if (StringUtils.hasText(String.valueOf(taskDTO.getObserves()))) {
                taskEntity.setObserverIds(taskDTO.getObserves());
            }

            return taskRepository.save(taskEntity);
        });
    }

    public Mono<Void> deleteTaskById(String id) {
        return taskRepository.deleteById(id);
    }

    public Mono<TaskEntity> addObserve(String id, ObserveDTO observeDTO) {
        return getTaskById(id).flatMap(taskEntity -> {
            Set<String> observeList = taskEntity.getObserverIds();

            taskEntity.setUpdatedAt(Instant.now());

            observeList.add(observeDTO.getObserveId());
            taskEntity.setObserverIds(observeList);

            return taskRepository.save(taskEntity);
        });
    }
}
