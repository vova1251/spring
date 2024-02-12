package com.example.NewsList.mapper;

import com.example.NewsList.dto.users.UserResponse;
import com.example.NewsList.dto.users.UpsertUser;
import com.example.NewsList.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", expression = "java(userEntity.getRoles().stream().findFirst().get().toString())")
    UserResponse toUserResponse(UserEntity userEntity);

//    @Mapping(target = "roles", expression = "java(Collections.singletonList(role))")
//    UserEntity toUserEntity(UpsertUser newUser);
}
