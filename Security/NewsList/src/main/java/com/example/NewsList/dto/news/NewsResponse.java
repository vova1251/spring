package com.example.NewsList.dto.news;

import com.example.NewsList.dto.review.ReviewResponse;
import com.example.NewsList.dto.users.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class NewsResponse {

    private Integer id;
    private UserResponse user;
    private NewsCategoryResponse newsCategory;
    private String text;
    private List<ReviewResponse> reviews;

}
