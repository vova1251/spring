package com.example.NewsList.controller;

import com.example.NewsList.dto.error.MyErrorResponse;
import com.example.NewsList.dto.users.UserResponse;
import com.example.NewsList.dto.users.UpsertUser;
import com.example.NewsList.error.ApiError;
import com.example.NewsList.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    public ResponseEntity<List<UserResponse>> getUserList(
            @Valid @RequestParam Integer offset, @Valid @RequestParam Integer limit) {

        return ResponseEntity.ok().body(userService.getUsers(offset, limit));
    }

    @PostMapping
    public ResponseEntity<UserResponse> addNewUser(@Valid @RequestBody UpsertUser newUser) {
        UserResponse user = userService.saveNewUser(newUser);

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<UserResponse> updateUser(@Valid @PathVariable Integer id, @Valid @RequestBody UpsertUser user) throws ApiError {
        UserResponse updateUser = userService.updateUser(id, user);

        return new ResponseEntity<>(updateUser, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUser(@Valid @PathVariable Integer id) throws ApiError {
        userService.deleteUser(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
