package com.example.NewsList.mapper;

import com.example.NewsList.dto.review.ReviewResponse;
import com.example.NewsList.entity.ReviewsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = UserMapper.class)
public interface ReviewMapper {

    @Mapping(source = "user.name", target = "userName")
    ReviewResponse toReviewResponse(ReviewsEntity reviewsEntity);

}
