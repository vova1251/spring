package com.example.demo.service;

import com.example.demo.dto.UserDTO;
import com.example.demo.entity.UserEntity;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
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
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

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

            if (StringUtils.hasText(userDTO.getPassword())) {
                userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            }

            if (StringUtils.hasText(String.valueOf(userDTO.getRoles()))) {
                userEntity.setRoles(userDTO.getRoles());
            }

            return userRepository.save(userEntity);
        });
    }

    public Mono<Void> deleteUserById(String id) {
        return userRepository.deleteById(id);
    }

    public Mono<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
