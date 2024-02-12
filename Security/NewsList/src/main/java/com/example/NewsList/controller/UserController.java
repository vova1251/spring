package com.example.NewsList.controller;

import com.example.NewsList.aop.AuthAccess;
import com.example.NewsList.dto.users.UserResponse;
import com.example.NewsList.dto.users.UpsertUser;
import com.example.NewsList.error.ApiError;
import com.example.NewsList.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserResponse>> getUserList(
            @Valid @RequestParam Integer offset, @Valid @RequestParam Integer limit) {

        return ResponseEntity.ok().body(userService.getUsers(offset, limit));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_MODERATOR')")
    @AuthAccess
    public ResponseEntity<UserResponse> getUserById(@Valid @PathVariable Integer id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<UserResponse> addNewUser(@Valid @RequestBody UpsertUser newUser) {
        UserResponse user = userService.saveNewUser(newUser);

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_MODERATOR')")
    @AuthAccess
    public ResponseEntity<UserResponse> updateUser(@Valid @PathVariable Integer id, @Valid @RequestBody UpsertUser user) throws ApiError {
        UserResponse updateUser = userService.updateUser(id, user);

        return new ResponseEntity<>(updateUser, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_MODERATOR')")
    @AuthAccess
    public ResponseEntity<Void> deleteUser(@Valid @PathVariable Integer id) throws ApiError {
        userService.deleteUser(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
