package com.example.NewsList.repository;

import com.example.NewsList.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    @EntityGraph(attributePaths = {"roles"})
    Optional<UserEntity> findByName(String name);

}
