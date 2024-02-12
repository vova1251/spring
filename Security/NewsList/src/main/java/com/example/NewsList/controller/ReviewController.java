package com.example.NewsList.controller;

import com.example.NewsList.aop.AuthAccess;
import com.example.NewsList.dto.review.ReviewResponse;
import com.example.NewsList.dto.review.ReviewsListResponse;
import com.example.NewsList.dto.review.UpsertReview;
import com.example.NewsList.error.ApiError;
import com.example.NewsList.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_MODERATOR')")
    public ResponseEntity<ReviewsListResponse> getReviews(@Valid @RequestParam Integer offset,
                                                          @Valid @RequestParam Integer limit) {
        return ResponseEntity.ok(reviewService.getReviews(offset, limit));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_MODERATOR')")
    public ResponseEntity<ReviewResponse> addReview(@Valid @RequestBody UpsertReview upsertReview) throws ApiError {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.addReview(upsertReview));
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_MODERATOR')")
    @AuthAccess
    public ResponseEntity<ReviewResponse> updateResponse(@Valid @PathVariable Integer id,
                                                         @Valid @RequestBody UpsertReview upsertReview) throws ApiError {
        return ResponseEntity.ok(reviewService.updateReview(id, upsertReview));
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_MODERATOR')")
    @AuthAccess
    public ResponseEntity<Void> deleteReview(@Valid @PathVariable Integer id) throws ApiError {
        reviewService.deleteReview(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
