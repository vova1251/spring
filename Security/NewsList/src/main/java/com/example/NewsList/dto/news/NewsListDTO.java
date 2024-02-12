package com.example.NewsList.dto.news;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class NewsListDTO {

    private List<NewsResponseWOReview> newsList;
    private Long count;

}
