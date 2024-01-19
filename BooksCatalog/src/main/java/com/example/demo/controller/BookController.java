package com.example.demo.controller;

import com.example.demo.dto.BookListResponse;
import com.example.demo.dto.BookResponse;
import com.example.demo.dto.UpsertBookRequest;
import com.example.demo.error.ApiError;
import com.example.demo.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(value = "/find-by-name-and-author")
    public ResponseEntity<BookResponse> getBookByNameAndAuthor(@RequestParam String bookName,
                                                               @RequestParam String authorName) {
        return ResponseEntity.ok(bookService.getBookByNameAndAuthor(bookName, authorName));
    }

    @GetMapping("/find-by-category")
    public ResponseEntity<BookListResponse> getBookListByCategoryName(@RequestParam String CategoryName) {
        return ResponseEntity.ok(bookService.getBookListByCategoryName(CategoryName));
    }

    @PostMapping
    public ResponseEntity<BookResponse> addBook(@Valid @RequestBody UpsertBookRequest upsertBook) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.addBook(upsertBook));
    }

    @PutMapping("{id}")
    public ResponseEntity<BookResponse> updateBook(@Valid @PathVariable Long id,
                                                   @Valid @RequestBody UpsertBookRequest upsertBookRequest) throws ApiError {
        return ResponseEntity.ok(bookService.updateBook(id, upsertBookRequest));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteBook(@Valid @PathVariable Long id) throws ApiError {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

}
