package com.example.NewsList.dto.news;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpsertUpdatedNews {

    @NotBlank(message = "Текст новости не должен быть пустым")
    private String text;

}
