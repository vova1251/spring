package com.example.demo.service;

import com.example.demo.config.CacheProperties;
import com.example.demo.dto.BookListResponse;
import com.example.demo.dto.BookResponse;
import com.example.demo.dto.UpsertBookRequest;
import com.example.demo.entity.BookEntity;
import com.example.demo.entity.CategoryEntity;
import com.example.demo.error.ApiError;
import com.example.demo.mapper.BookMapper;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@CacheConfig(cacheManager = "RedisCacheManager")
public class BookService {

    private final BookMapper bookMapper;
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public BookService(BookMapper bookMapper, BookRepository bookRepository, CategoryRepository categoryRepository) {
        this.bookMapper = bookMapper;
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
    }

    @Caching(evict = {
            @CacheEvict(value = CacheProperties.CacheNames.NANE_AND_AUTHOR, key = "#upsertBook.getBookName() + #upsertBook.getAuthor()"),
            @CacheEvict(value = CacheProperties.CacheNames.CATEGORY, key = "#upsertBook.getCategoryName()")
    })
    @Transactional
    public BookResponse addBook(UpsertBookRequest upsertBook) {
        BookEntity book = new BookEntity();
        CategoryEntity category = new CategoryEntity();

        category.setName(upsertBook.getCategoryName());
        categoryRepository.save(category);

        book.setName(upsertBook.getBookName());
        book.setAuthor(upsertBook.getAuthor());
        book.setCategory(category);

        return bookMapper.toBookResponse(bookRepository.save(book));
    }

    @Caching(evict = {
            @CacheEvict(value = CacheProperties.CacheNames.NANE_AND_AUTHOR, key = "#upsertBookRequest.getBookName() + #upsertBookRequest.getAuthor()"),
            @CacheEvict(value = CacheProperties.CacheNames.CATEGORY, key = "#upsertBookRequest.getCategoryName()")
    })
    @Transactional
    public BookResponse updateBook(Long id, UpsertBookRequest upsertBookRequest) throws ApiError {
        Optional<BookEntity> optionalBook = bookRepository.findById(id);
        if (!optionalBook.isPresent()) {
            throw new ApiError("Book with id " + id + " is not found");
        }

        BookEntity book = optionalBook.get();
        CategoryEntity category = book.getCategory();

        category.setName(upsertBookRequest.getCategoryName());
        categoryRepository.save(category);

        book.setName(upsertBookRequest.getBookName());
        book.setAuthor(upsertBookRequest.getAuthor());

        return bookMapper.toBookResponse(bookRepository.save(book));
    }

    @Caching(evict = {
            @CacheEvict(value = CacheProperties.CacheNames.NANE_AND_AUTHOR, allEntries = true),
            @CacheEvict(value = CacheProperties.CacheNames.CATEGORY, allEntries = true)
    })
    @Transactional
    public void deleteBook(Long id) throws ApiError {
        Optional<BookEntity> optionalBook = bookRepository.findById(id);
        if (!optionalBook.isPresent()) {
            throw new ApiError("Book with id " + id + " is not found");
        }

        bookRepository.deleteById(id);
    }

    @Cacheable(value = CacheProperties.CacheNames.NANE_AND_AUTHOR, key = "#bookName + #authorName")
    public BookResponse getBookByNameAndAuthor(String bookName, String authorName) {
        return bookMapper.toBookResponse(bookRepository.findByNameAndAuthor(bookName, authorName));
    }

    @Cacheable(value = CacheProperties.CacheNames.CATEGORY, key = "#categoryName")
    public BookListResponse getBookListByCategoryName(String categoryName) {
        List<CategoryEntity> categories = categoryRepository.findAllByName(categoryName);
        if (categories.isEmpty()) {
            return new BookListResponse(new ArrayList<>());
        }

        List<BookEntity> books = bookRepository.findAllByCategoryIdIn(
                categories.stream()
                        .map(CategoryEntity::getId)
                        .toList());

        return bookMapper.toBookListResponse(books);

    }
}
