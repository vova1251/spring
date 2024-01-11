package com.example.NewsList.service;

import com.example.NewsList.aop.AuthAccess;
import com.example.NewsList.dto.news.*;
import com.example.NewsList.entity.CategoryEntity;
import com.example.NewsList.entity.NewsEntity;
import com.example.NewsList.entity.UserEntity;
import com.example.NewsList.error.ApiError;
import com.example.NewsList.mapper.NewsMapper;
import com.example.NewsList.repository.NewsCategoryRepository;
import com.example.NewsList.repository.NewsRepository;
import com.example.NewsList.repository.NewsSpecification;
import com.example.NewsList.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NewsService {
    
    private final NewsRepository newsRepository;
    private final UserRepository userRepository;
    private final NewsCategoryRepository newsCategoryRepository;
    private final NewsMapper newsMapper;

    @Autowired
    public NewsService(NewsRepository newsRepository, UserRepository userRepository, NewsCategoryRepository newsCategoryRepository, NewsMapper newsMapper) {
        this.newsRepository = newsRepository;
        this.userRepository = userRepository;
        this.newsCategoryRepository = newsCategoryRepository;
        this.newsMapper = newsMapper;
    }

    public NewsListDTO getNews(Integer offset, Integer limit) {
        Pageable pageable = PageRequest.of(offset, limit);
        Page<NewsEntity> news = newsRepository.findAll(pageable);
        List<NewsResponseWOReview> newsResponseList = news.getContent()
                .stream().map(newsMapper::toNewsResponseWOReview).toList();
        return new NewsListDTO(newsResponseList, news.getTotalElements());
    }

    public NewsResponse addNews(UpsertNews upsertNews) throws ApiError {
        Optional<UserEntity> optionalUser = userRepository.findById(upsertNews.getUserId());
        if (optionalUser.isEmpty()) {
            throw new ApiError("Нет пользователя с id: " + upsertNews.getUserId());
        }
        Optional<CategoryEntity> optionalCategory = newsCategoryRepository.findById(upsertNews.getCategoryId());
        if (optionalCategory.isEmpty()) {
            throw new ApiError("Нет категории новостей с id: " + upsertNews.getCategoryId());
        }

        UserEntity user = optionalUser.get();
        CategoryEntity category = optionalCategory.get();

        NewsEntity news = new NewsEntity();
        news.setText(upsertNews.getText());
        news.setUser(user);
        news.setCategoryEntity(category);

        newsRepository.save(news);
        return newsMapper.toNewsResponse(news);
    }

    @AuthAccess
    public NewsResponse updateNews(Integer id, UpsertUpdatedNews upsertUpdatedNews) throws ApiError {
        Optional<NewsEntity> optionalNews = newsRepository.findById(id);
        if (optionalNews.isEmpty()) {
            throw new ApiError("Новость с ID: " + id + " не существует");
        }

        NewsEntity news = optionalNews.get();
        news.setText(upsertUpdatedNews.getText());

        newsRepository.save(news);
        return newsMapper.toNewsResponse(news);
    }

    public void deleteNews(Integer id) throws ApiError {
        Optional<NewsEntity> optionalNews = newsRepository.findById(id);
        if (optionalNews.isEmpty()) {
            throw new ApiError("Новости с ID " + id + " не существует");
        }

        newsRepository.delete(optionalNews.get());
    }

    public NewsResponse getNewsById(Integer id) throws ApiError {
        Optional<NewsEntity> optionalNews = newsRepository.findById(id);
        if (optionalNews.isEmpty()) {
            throw new ApiError("Новости с ID " + id + " не найдено");
        }

        return newsMapper.toNewsResponse(optionalNews.get());
    }

    public NewsListDTO getFilteredNews(Integer limit, Integer offset, NewsFilter newsFilter) {
        Pageable pageable = PageRequest.of(offset, limit);

        Page<NewsEntity> news = newsRepository.findAll(NewsSpecification.withFilter(newsFilter), pageable);
        List<NewsResponseWOReview> newsResponseList = news.getContent()
                .stream().map(newsMapper::toNewsResponseWOReview).toList();
        return new NewsListDTO(newsResponseList, news.getTotalElements());
    }
}
