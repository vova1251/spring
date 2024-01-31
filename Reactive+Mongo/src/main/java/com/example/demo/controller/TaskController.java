package com.example.demo.controller;

import com.example.demo.dto.ObserveDTO;
import com.example.demo.dto.TaskDTO;
import com.example.demo.entity.TaskEntity;
import com.example.demo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public Flux<TaskEntity> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<TaskEntity>> getTaskById(@PathVariable String id) {
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<TaskEntity>> addTask(@RequestBody TaskDTO taskDTO) {
        return taskService.addTask(taskDTO)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<TaskEntity>> updateTask(@PathVariable String id, @RequestBody TaskDTO taskDTO) {
        return taskService.updateTaskById(id, taskDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/observe/{id}")
    public Mono<ResponseEntity<TaskEntity>> addObserve(@PathVariable String id, @RequestBody ObserveDTO observeDTO) {
        return taskService.addObserve(id, observeDTO)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteTaskById(@PathVariable String id) {
        return taskService.deleteTaskById(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }

}
