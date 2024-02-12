package com.example.demo.mapper;

import com.example.demo.dto.UserDTO;
import com.example.demo.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserDTO fromUserEntity2UserDTO(UserEntity userEntity);
    UserEntity fromUserDTO2UserEntity(UserDTO userDTO);

}
