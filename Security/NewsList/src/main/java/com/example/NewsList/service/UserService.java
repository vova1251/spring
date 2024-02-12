package com.example.NewsList.service;

import com.example.NewsList.dto.users.UserResponse;
import com.example.NewsList.dto.users.UpsertUser;
import com.example.NewsList.entity.Role;
import com.example.NewsList.entity.UserEntity;
import com.example.NewsList.error.ApiError;
import com.example.NewsList.mapper.UserMapper;
import com.example.NewsList.repository.UserRepository;
import com.example.NewsList.security.AppUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    public UserEntity findByUsername(String username) {
        return userRepository.findByName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    public List<UserResponse> getUsers(Integer offset, Integer limit) {
        Pageable pageable = PageRequest.of(offset, limit);

        Page<UserEntity> users = userRepository.findAll(pageable);

        if (users.isEmpty()) return null;

        return users.getContent().stream().map(userMapper::toUserResponse).toList();
    }

    public UserResponse saveNewUser(UpsertUser newUser) {

        UserEntity user = new UserEntity();
        Role role = Role.from(newUser.getRole());

        user.setName(newUser.getName());
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        user.setEmail(newUser.getEmail());
        user.setRoles(Collections.singletonList(role));
        role.setUser(user);

        userRepository.saveAndFlush(user);

        return userMapper.toUserResponse(user);
    }

    public UserResponse updateUser(Integer id, UpsertUser user) throws ApiError {
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            throw new ApiError("User not found in database");
        }

        UserEntity updatingUser = optionalUser.get();
        Role role = Role.from(user.getRole());
        role.setUser(updatingUser);

        updatingUser.setName(user.getName());
        updatingUser.setEmail(user.getEmail());
        updatingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        updatingUser.setRoles(Collections.singletonList(role));

        userRepository.saveAndFlush(updatingUser);

        return userMapper.toUserResponse(updatingUser);
    }

    public void deleteUser(Integer id) throws ApiError {
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new ApiError("User not found in database");
        }

        userRepository.deleteById(id);
    }

    public UserResponse getUserById(Integer id) {
        return userMapper.toUserResponse(userRepository.findById(id).get());
    }

    public UserEntity getCurrentUser() {
        AppUserDetails appUserDetails =
                (AppUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return appUserDetails.getUser();
    }
}
