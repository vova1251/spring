package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BookListResponse implements Serializable {

    private List<BookResponse> bookList = new ArrayList<>();
    private Integer count;

    public BookListResponse(List<BookResponse> bookList) {
        this.bookList = bookList;
        this.count = bookList.size();
    }
}
