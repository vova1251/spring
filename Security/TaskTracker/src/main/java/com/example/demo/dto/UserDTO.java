package com.example.demo.dto;

import com.example.demo.enums.RoleType;
import lombok.Data;

import java.util.Set;

@Data
public class UserDTO {

    private String email;
    private String username;
    private String password;
    private Set<RoleType> roles;

}
