package com.example.demo.entity;

import com.example.demo.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "user")
public class UserEntity {

    @Id
    private String id;
    private String email;
    private String username;
    private String password;
    @Field("roles")
    private Set<RoleType> roles = new HashSet<>();


}
