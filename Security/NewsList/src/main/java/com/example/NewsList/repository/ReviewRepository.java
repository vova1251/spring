package com.example.NewsList.repository;

import com.example.NewsList.entity.ReviewsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewsEntity, Integer> {

}
