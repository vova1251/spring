package com.example.NewsList.dto.review;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpsertReview {

    @NotBlank(message = "Текст должен быть заполнен")
    private String text;
    @NotNull(message = "Поле userId должно быть заполнено")
    @Positive(message = "Поле userId должно быть > 0")
    private Integer userId;
    @NotNull(message = "Поле newsId должно быть заполнено")
    @Positive(message = "Поле newsId должно быть > 0")
    private Integer newsId;

}
