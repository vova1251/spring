package com.example.NewsList.service;

import com.example.NewsList.dto.users.UserResponse;
import com.example.NewsList.dto.users.UpsertUser;
import com.example.NewsList.entity.UserEntity;
import com.example.NewsList.error.ApiError;
import com.example.NewsList.mapper.UserMapper;
import com.example.NewsList.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }
    public List<UserResponse> getUsers(Integer offset, Integer limit) {
        Pageable pageable = PageRequest.of(offset, limit);

        Page<UserEntity> users = userRepository.findAll(pageable);

        if (users.isEmpty()) return null;

        return users.getContent().stream().map(userMapper::toUserResponse).toList();
    }

    public UserResponse saveNewUser(UpsertUser newUser) {
        UserEntity user = userMapper.toUserEntity(newUser);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse updateUser(Integer id, UpsertUser user) throws ApiError {
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            throw new ApiError("User not found in database");
        }

        UserEntity updatingUser = optionalUser.get();
        updatingUser.setName(user.getName());
        updatingUser.setEmail(user.getEmail());

        return userMapper.toUserResponse(userRepository.save(updatingUser));
    }

    public void deleteUser(Integer id) throws ApiError {
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new ApiError("User not found in database");
        }

        userRepository.deleteById(id);
    }
}
