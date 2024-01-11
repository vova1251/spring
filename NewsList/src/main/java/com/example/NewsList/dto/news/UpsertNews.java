package com.example.NewsList.dto.news;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpsertNews {

    @NotBlank(message = "Текст новости должен быть не пустым")
    private String text;
    @NotNull(message = "Значение userId должно быть заполнено")
    @Positive(message = "userId должен быть > 0")
    private Integer userId;
    @NotNull(message = "Значение categoryId должно быть заполнено")
    @Positive(message = "categoryId должен быть > 0")
    private Integer categoryId;

}
