package com.example.NewsList.dto.review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class ReviewsListResponse {

    private List<ReviewResponse> reviewResponseList;
    private Long count;

}
