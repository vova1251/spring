package com.example.NewsList.dto.news;

import com.example.NewsList.dto.users.UserResponse;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewsResponseWOReview {

    private Integer id;
    private UserResponse user;
    private NewsCategoryResponse newsCategory;
    private String text;
    private Integer reviewsCount;

}
