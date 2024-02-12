package com.example.NewsList.service;

import com.example.NewsList.dto.news.NewsCategoryResponse;
import com.example.NewsList.dto.news.UpsertCategory;
import com.example.NewsList.entity.CategoryEntity;
import com.example.NewsList.entity.UserEntity;
import com.example.NewsList.error.ApiError;
import com.example.NewsList.mapper.NewsCategoryMapper;
import com.example.NewsList.repository.NewsCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NewsCategoryService {

    private final NewsCategoryRepository newsCategoryRepository;
    private final NewsCategoryMapper newsCategoryMapper;

    @Autowired
    public NewsCategoryService(NewsCategoryRepository newsCategoryRepository, NewsCategoryMapper newsCategoryMapper) {
        this.newsCategoryRepository = newsCategoryRepository;
        this.newsCategoryMapper = newsCategoryMapper;
    }

    public List<NewsCategoryResponse> getCategories(Integer offset, Integer limit) {
        Pageable pageable = PageRequest.of(offset, limit);

        return newsCategoryRepository.findAll(pageable).stream().map(newsCategoryMapper::toNewsCategoryResponse).toList();
    }

    public NewsCategoryResponse addCategory(UpsertCategory upsertCategory) {
        CategoryEntity categoryEntity = newsCategoryMapper.toCategoryEntity(upsertCategory);
        return newsCategoryMapper.toNewsCategoryResponse(newsCategoryRepository.save(categoryEntity));
    }

    public NewsCategoryResponse updateCategory(Integer id,UpsertCategory upsertCategory) throws ApiError {
        Optional<CategoryEntity> optionalCategory = newsCategoryRepository.findById(id);
        if (!optionalCategory.isPresent()) {
            throw new ApiError("Category not found in database");
        }

        CategoryEntity updatingCategory = optionalCategory.get();
        updatingCategory.setCategory(upsertCategory.getCategoryName());

        return newsCategoryMapper.toNewsCategoryResponse(newsCategoryRepository.save(updatingCategory));
    }

    public void deleteCategoryById(Integer id) throws ApiError {
        Optional<CategoryEntity> optionalCategory = newsCategoryRepository.findById(id);
        if (!optionalCategory.isPresent()) {
            throw new ApiError("Category not found in database");
        }

        newsCategoryRepository.deleteById(id);
    }

    public NewsCategoryResponse getCategoryById(Integer id) throws ApiError {
        Optional<CategoryEntity> optionalCategory = newsCategoryRepository.findById(id);
        if (!optionalCategory.isPresent()) {
            throw new ApiError("Category not found in database");
        }

        return newsCategoryMapper.toNewsCategoryResponse(optionalCategory.get());
    }
}
