package com.example.demo.service;

import com.example.demo.dto.UserDTO;
import com.example.demo.entity.UserEntity;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public Flux<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public Mono<UserEntity> getUserById(String id) {
        return userRepository.findById(id);
    }

    public Mono<UserEntity> saveUser(UserDTO userDTO) {
        UserEntity user = userMapper.fromUserDTO2UserEntity(userDTO);
        user.setId(UUID.randomUUID().toString());

        return userRepository.save(user);
    }

    public Mono<UserEntity> updateUser(String id, UserDTO userDTO) {
        return getUserById(id).flatMap(userEntity -> {

            if (StringUtils.hasText(userDTO.getUsername())) {
                userEntity.setUsername(userDTO.getUsername());
            }

            if (StringUtils.hasText(userDTO.getEmail())) {
                userEntity.setEmail(userDTO.getEmail());
            }

            return userRepository.save(userEntity);
        });
    }

    public Mono<Void> deleteUserById(String id) {
        return userRepository.deleteById(id);
    }

}
