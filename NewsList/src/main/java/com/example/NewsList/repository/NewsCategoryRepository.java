package com.example.NewsList.repository;

import com.example.NewsList.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsCategoryRepository extends JpaRepository<CategoryEntity, Integer> {



}
