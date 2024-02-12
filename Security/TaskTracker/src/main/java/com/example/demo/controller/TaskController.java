package com.example.demo.controller;

import com.example.demo.dto.ObserveDTO;
import com.example.demo.dto.TaskDTO;
import com.example.demo.entity.TaskEntity;
import com.example.demo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MANAGER')")
    @GetMapping
    public Flux<TaskEntity> getAllTasks() {
        Flux<TaskEntity> a = taskService.getAllTasks();
        return a;
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MANAGER')")
    @GetMapping("/{id}")
    public Mono<ResponseEntity<TaskEntity>> getTaskById(@PathVariable String id) {
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PostMapping
    public Mono<ResponseEntity<TaskEntity>> addTask(@RequestBody TaskDTO taskDTO) {
        return taskService.addTask(taskDTO)
                .map(ResponseEntity::ok);
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PutMapping("/{id}")
    public Mono<ResponseEntity<TaskEntity>> updateTask(@PathVariable String id, @RequestBody TaskDTO taskDTO) {
        return taskService.updateTaskById(id, taskDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MANAGER')")
    @PutMapping("/observe/{id}")
    public Mono<ResponseEntity<TaskEntity>> addObserve(@PathVariable String id, @RequestBody ObserveDTO observeDTO) {
        return taskService.addObserve(id, observeDTO)
                .map(ResponseEntity::ok);
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteTaskById(@PathVariable String id) {
        return taskService.deleteTaskById(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }

}
