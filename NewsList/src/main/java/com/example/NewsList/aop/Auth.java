package com.example.NewsList.aop;

import com.example.NewsList.entity.NewsEntity;
import com.example.NewsList.entity.ReviewsEntity;
import com.example.NewsList.error.ApiError;
import com.example.NewsList.repository.NewsRepository;
import com.example.NewsList.repository.ReviewRepository;
import com.example.NewsList.service.NewsService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Aspect
@Component
public class Auth {

    private final NewsRepository newsRepository;
    private final ReviewRepository reviewRepository;

    @Autowired
    public Auth(NewsRepository newsRepository, ReviewRepository reviewRepository) {
        this.newsRepository = newsRepository;
        this.reviewRepository = reviewRepository;
    }

    @Before("@annotation(AuthAccess)")
    public void preAuth(JoinPoint joinPoint) throws ApiError {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        String tUser = request.getHeader("user-id");
        if (tUser == null) {
            throw new ApiError("ОРшибка авторизации");
        }
        Integer procUser = Integer.parseInt(request.getHeader("user-id"));

        Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);

        if (path.matches("^/news/\\d+$")) {
            Integer newsId = Integer.parseInt(pathVariables.get("id"));
            Optional<NewsEntity> optionalNews = newsRepository.findById(newsId);
            if (optionalNews.isEmpty()) {
                throw new ApiError("Новости с ID " + newsId + " не существует в БД");
            }
            if (optionalNews.get().getUser().getId() != procUser) {
                throw new ApiError("Действие достуно только для автора");
            }
        } else if (path.matches("^/review/\\d+$")) {
            Integer reviewsId = Integer.parseInt(pathVariables.get("id"));
            Optional<ReviewsEntity> optionalReview = reviewRepository.findById(reviewsId);
            if (optionalReview.isEmpty()) {
                throw new ApiError("Отзыва с ID " + reviewsId + " не существует в БД");
            }
            if (optionalReview.get().getUser().getId() != procUser) {
                throw new ApiError("Действие достуно только для автора");
            }
        } else {
            return;
        }

        System.out.println(procUser);
    }

}
