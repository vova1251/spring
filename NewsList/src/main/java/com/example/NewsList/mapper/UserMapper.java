package com.example.NewsList.mapper;

import com.example.NewsList.dto.users.UserResponse;
import com.example.NewsList.dto.users.UpsertUser;
import com.example.NewsList.entity.UserEntity;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toUserResponse(UserEntity userEntity);

    UserEntity toUserEntity(UpsertUser newUser);
}
