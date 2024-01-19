package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UpsertBookRequest {

    @NotBlank(message = "The book name must be specified")
    @NotEmpty(message = "The book name must be filled in")
    private String bookName;
    @NotBlank(message = "The author name must be specified")
    @NotEmpty(message = "The author name must be filled in")
    private String author;
    @NotBlank(message = "The category must be specified")
    @NotEmpty(message = "The category must be filled in")
    private String categoryName;

}
