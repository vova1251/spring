package com.example.demo.repository;

import com.example.demo.entity.TaskEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends ReactiveMongoRepository<TaskEntity, String> {



}
