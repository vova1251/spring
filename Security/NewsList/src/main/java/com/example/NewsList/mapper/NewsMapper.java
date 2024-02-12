package com.example.NewsList.mapper;

import com.example.NewsList.dto.news.NewsResponse;
import com.example.NewsList.dto.news.NewsResponseWOReview;
import com.example.NewsList.entity.NewsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {UserMapper.class, NewsCategoryMapper.class})
public interface NewsMapper {

    @Mapping(source = "categoryEntity", target = "newsCategory")
    @Mapping(source = "reviewsEntities", target = "reviews")
    NewsResponse toNewsResponse(NewsEntity newsEntity);

    @Mapping(source = "categoryEntity", target = "newsCategory")
    @Mapping(target = "reviewsCount", expression = "java(newsEntity.getReviewsEntities()!= null ? newsEntity.getReviewsEntities().size() : 0)")
    NewsResponseWOReview toNewsResponseWOReview(NewsEntity newsEntity);

}
