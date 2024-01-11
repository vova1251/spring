package com.example.NewsList.repository;

import com.example.NewsList.dto.news.NewsFilter;
import com.example.NewsList.entity.NewsEntity;
import org.springframework.data.jpa.domain.Specification;

public interface NewsSpecification {

    static Specification<NewsEntity> withFilter(NewsFilter newsFilter) {

        return Specification.where(byUserId(newsFilter.getUserId()))
                .and(byCategoryId(newsFilter.getCategoryId()));

    }

    static Specification<NewsEntity> byUserId(Integer userId) {
        return ((root, query, criteriaBuilder) -> {
            if (userId == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("user").get("id"), userId);
        });
    }

    static Specification<NewsEntity> byCategoryId(Integer categoryId) {
        return ((root, query, criteriaBuilder) -> {
            if (categoryId == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("categoryEntity").get("id"), categoryId);
        });
    }

}
