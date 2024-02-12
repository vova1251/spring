package com.example.demo.mapper;

import com.example.demo.dto.TaskDTO;
import com.example.demo.entity.TaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {

    @Mapping(source = "observes", target = "observerIds")
    TaskEntity fromTaskDTO2TaskEntity(TaskDTO taskDTO);
    TaskDTO fromTaskEntity2TaskDTO(TaskEntity taskEntity);

}
