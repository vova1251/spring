package com.example.NewsList.service;

import com.example.NewsList.dto.review.ReviewResponse;
import com.example.NewsList.dto.review.ReviewsListResponse;
import com.example.NewsList.dto.review.UpsertReview;
import com.example.NewsList.entity.NewsEntity;
import com.example.NewsList.entity.ReviewsEntity;
import com.example.NewsList.entity.UserEntity;
import com.example.NewsList.error.ApiError;
import com.example.NewsList.mapper.ReviewMapper;
import com.example.NewsList.repository.NewsRepository;
import com.example.NewsList.repository.ReviewRepository;
import com.example.NewsList.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReviewService {

    private final UserRepository userRepository;
    private final NewsRepository newsRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final UserService userService;

    @Autowired
    public ReviewService(UserRepository userRepository, NewsRepository newsRepository, ReviewRepository reviewRepository, ReviewMapper reviewMapper, UserService userService) {
        this.userRepository = userRepository;
        this.newsRepository = newsRepository;
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
        this.userService = userService;
    }


    public ReviewsListResponse getReviews(Integer offset, Integer limit) {
        Pageable pageable = PageRequest.of(offset, limit);

        Page<ReviewsEntity> reviews = reviewRepository.findAll(pageable);

        return new ReviewsListResponse(reviews
                .stream().map(reviewMapper::toReviewResponse).toList(), reviews.getTotalElements());
    }

    public ReviewResponse addReview(UpsertReview upsertReview) throws ApiError {
        UserEntity user = userService.getCurrentUser();

        Optional<NewsEntity> optionalNews = newsRepository.findById(upsertReview.getNewsId());
        if (optionalNews.isEmpty()) {
            throw new ApiError("Новость с ID " + upsertReview.getNewsId() + " не найдена");
        }

        ReviewsEntity review = new ReviewsEntity();
        review.setText(upsertReview.getText());
        review.setUser(user);
        review.setNewsEntity(optionalNews.get());

        ReviewsEntity reviewsEntity = reviewRepository.save(review);
        return reviewMapper.toReviewResponse(reviewsEntity);
    }

    public ReviewResponse updateReview(Integer id, UpsertReview upsertReview) throws ApiError {
        Optional<ReviewsEntity> optionalReviewsEntity = reviewRepository.findById(id);
        if (optionalReviewsEntity.isEmpty()) {
            throw new ApiError("Отзыв с ID " + id + " не существует");
        }

        UserEntity user = userService.getCurrentUser();

        Optional<NewsEntity> optionalNews = newsRepository.findById(upsertReview.getNewsId());
        if (optionalNews.isEmpty()) {
            throw new ApiError("Новость с ID " + upsertReview.getNewsId() + " не найдена");
        }

        ReviewsEntity review = optionalReviewsEntity.get();
        review.setText(upsertReview.getText());
        review.setUser(user);
        review.setNewsEntity(optionalNews.get());

        ReviewsEntity reviewsEntity = reviewRepository.save(review);
        return reviewMapper.toReviewResponse(reviewsEntity);
    }

    public void deleteReview(Integer id) throws ApiError {
        Optional<ReviewsEntity> optionalReview = reviewRepository.findById(id);
        if (optionalReview.isEmpty()) {
            throw new ApiError("Отзыва с ID " + id + " не существует");
        }

        reviewRepository.deleteById(id);
    }
}
