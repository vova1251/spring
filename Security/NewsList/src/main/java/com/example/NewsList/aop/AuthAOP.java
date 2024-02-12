package com.example.NewsList.aop;

import com.example.NewsList.entity.NewsEntity;
import com.example.NewsList.entity.ReviewsEntity;
import com.example.NewsList.entity.UserEntity;
import com.example.NewsList.enums.RoleType;
import com.example.NewsList.error.ApiError;
import com.example.NewsList.repository.NewsRepository;
import com.example.NewsList.repository.ReviewRepository;
import com.example.NewsList.repository.UserRepository;
import com.example.NewsList.service.UserService;
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

import java.util.Map;
import java.util.Optional;

@Aspect
@Component
public class AuthAOP {

    private final NewsRepository newsRepository;
    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public AuthAOP(NewsRepository newsRepository, ReviewRepository reviewRepository, UserService userService, UserRepository userRepository) {
        this.newsRepository = newsRepository;
        this.reviewRepository = reviewRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Before("@annotation(AuthAccess)")
    public void preAuth(JoinPoint joinPoint) throws ApiError {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        UserEntity user = userService.getCurrentUser();

        Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);

        if (path.matches("^/news/\\d+$")) {
            Integer newsId = Integer.parseInt(pathVariables.get("id"));
            Optional<NewsEntity> optionalNews = newsRepository.findById(newsId);
            if (optionalNews.isEmpty()) {
                throw new ApiError("Новости с ID " + newsId + " не существует в БД");
            }

            if (joinPoint.getSignature().getName().equals("deleteNews")) {
                if (user.getRoles().stream().filter(role -> role.getRole().equals(RoleType.ROLE_USER)).toList().isEmpty()) {
                    if (optionalNews.get().getUser().getId() != user.getId()) {
                        throw new ApiError("Действие достуно только для автора");
                    }
                } else {
                    return;
                }
            }

            if (optionalNews.get().getUser().getId() != user.getId()) {
                throw new ApiError("Действие достуно только для автора");
            }

        } else if (path.matches("^/review/\\d+$")) {
            Integer reviewsId = Integer.parseInt(pathVariables.get("id"));
            Optional<ReviewsEntity> optionalReview = reviewRepository.findById(reviewsId);
            if (optionalReview.isEmpty()) {
                throw new ApiError("Отзыва с ID " + reviewsId + " не существует в БД");
            }

            if (joinPoint.getSignature().getName().equals("updateResponse")
                    && optionalReview.get().getUser().getId() != user.getId()) {
                throw new ApiError("Действие достуно только для автора");
            }

            if (joinPoint.getSignature().getName().equals("deleteReview")) {
                boolean roleAdmin = !user.getRoles().stream().filter(role -> role.getRole().equals(RoleType.ROLE_ADMIN)).toList().isEmpty();
                boolean roleModerator = !user.getRoles().stream().filter(role -> role.getRole().equals(RoleType.ROLE_MODERATOR)).toList().isEmpty();
                if (roleAdmin || roleModerator || user.getId() == optionalReview.get().getUser().getId()) {
                    return;
                } else {
                    throw new ApiError("Действие достуно только для автора или ROLE_ADMIN или ROLE_MODERATOR");
                }
            }

        } else if (path.matches("^/user/\\d+$")) {
            RoleType roleType = user.getRoles().stream().findAny().get().getRole();

            if (roleType.equals(RoleType.ROLE_ADMIN) || roleType.equals(RoleType.ROLE_MODERATOR)) {
                return;
            }

            Integer userId = Integer.parseInt(pathVariables.get("id"));
            Optional<UserEntity> optionalUser = userRepository.findById(userId);
            if (optionalUser.isEmpty()) {
                throw new ApiError("Пользователь с ID " + userId + " не существует в БД");
            }
            if (optionalUser.get().getId() != user.getId()) {
                throw new ApiError("Действие достуно только для автора");
            }
        }
    }

}
