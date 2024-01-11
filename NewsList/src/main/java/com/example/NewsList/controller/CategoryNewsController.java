package com.example.NewsList.controller;

import com.example.NewsList.dto.news.NewsCategoryResponse;
import com.example.NewsList.dto.news.UpsertCategory;
import com.example.NewsList.error.ApiError;
import com.example.NewsList.service.NewsCategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category-news")
public class CategoryNewsController {

    private final NewsCategoryService newsCategoryService;

    @Autowired
    public CategoryNewsController(NewsCategoryService newsCategoryService) {
        this.newsCategoryService = newsCategoryService;
    }

    @GetMapping
    public ResponseEntity<List<NewsCategoryResponse>> getCategories(
            @Valid @RequestParam Integer offset, @Valid @RequestParam Integer limit) {

        return ResponseEntity.ok(newsCategoryService.getCategories(offset, limit));

    }

    @PostMapping
    public ResponseEntity<NewsCategoryResponse> addCategory(@Valid @RequestBody UpsertCategory upsertCategory) {

        return ResponseEntity.status(HttpStatus.CREATED).body(
                newsCategoryService.addCategory(upsertCategory)
        );

    }

    @PutMapping("{id}")
    public ResponseEntity<NewsCategoryResponse> updateCategory(
            @Valid @PathVariable Integer id, @Valid @RequestBody UpsertCategory upsertCategory) throws ApiError {

        return ResponseEntity.ok(newsCategoryService.updateCategory(id, upsertCategory));

    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCategory(@Valid @PathVariable Integer id) throws ApiError {

        newsCategoryService.deleteCategoryById(id);

        return ResponseEntity.ok().build();

    }

}
