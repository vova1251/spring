package com.example.NewsList.controller;

import com.example.NewsList.aop.AuthAccess;
import com.example.NewsList.dto.news.*;
import com.example.NewsList.error.ApiError;
import com.example.NewsList.service.NewsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/news")
public class NewsController {

    private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_MODERATOR')")
    public ResponseEntity<NewsListDTO> getNewsList(
            @Valid @RequestParam Integer offset, @Valid @RequestParam Integer limit) {

        return ResponseEntity.ok(newsService.getNews(offset, limit));

    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_MODERATOR')")
    public ResponseEntity<NewsResponse> getNewsById(@Valid @PathVariable Integer id) throws ApiError {
        return ResponseEntity.ok(newsService.getNewsById(id));
    }

    @GetMapping("/filter")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_MODERATOR')")
    public ResponseEntity<NewsListDTO> getFilteredNews(@Valid @RequestParam Integer offset
            , @Valid @RequestParam Integer limit, NewsFilter newsFilter) {
        return ResponseEntity.ok(newsService.getFilteredNews(limit, offset, newsFilter));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_MODERATOR')")
    public ResponseEntity<NewsResponse> addNews(@Valid @RequestBody UpsertNews upsertNews) throws ApiError {
        return ResponseEntity.ok(newsService.addNews(upsertNews));
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_MODERATOR')")
    @AuthAccess
    public ResponseEntity<NewsResponse> updateNews(
            @Valid @PathVariable Integer id, @Valid @RequestBody UpsertUpdatedNews upsertUpdatedNews) throws ApiError {

        return ResponseEntity.ok(newsService.updateNews(id, upsertUpdatedNews));
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_MODERATOR')")
    @AuthAccess
    public ResponseEntity<Void> deleteNews(@Valid @PathVariable Integer id) throws ApiError {
        newsService.deleteNews(id);

        return ResponseEntity.ok().build();
    }

}
