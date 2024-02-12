package com.example.NewsList.dto.news;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpsertCategory {

    @NotNull(message = "Имя категории новостей должно быть заполнено")
    private String categoryName;

}
