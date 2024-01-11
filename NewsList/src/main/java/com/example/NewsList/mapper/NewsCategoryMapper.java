package com.example.NewsList.mapper;

import com.example.NewsList.dto.news.NewsCategoryResponse;
import com.example.NewsList.dto.news.UpsertCategory;
import com.example.NewsList.entity.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NewsCategoryMapper {

    @Mapping(source = "categoryName", target = "category")
    CategoryEntity toCategoryEntity(UpsertCategory upsertCategory);
    @Mapping(source = "category", target = "categoryName")
    NewsCategoryResponse toNewsCategoryResponse(CategoryEntity categoryEntity);

}
