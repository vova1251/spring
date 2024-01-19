package com.example.demo.repository;

import com.example.demo.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {

    BookEntity findByNameAndAuthor(String bookName, String authorName);

    List<BookEntity> findAllByCategoryIdIn(List<Long> categoriesIds);
}
