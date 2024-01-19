package com.example.demo.mapper;

import com.example.demo.dto.BookListResponse;
import com.example.demo.dto.BookResponse;
import com.example.demo.entity.BookEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookMapper {

    @Mapping(target = "category", expression = "java(book.getCategory().getName())")
    BookResponse toBookResponse(BookEntity book);

    default BookListResponse toBookListResponse(List<BookEntity> books) {
        return new BookListResponse(books.stream().map(this::toBookResponse).toList());
    }
}
