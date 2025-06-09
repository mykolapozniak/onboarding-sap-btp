package com.example.java.tutorial.controller;

import com.example.java.tutorial.model.Book;
import com.example.java.tutorial.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * BookController is a REST controller that handles requests related to books.
 * It provides endpoints for managing book resources.
 */
@RestController
@AllArgsConstructor
@RequestMapping("books")
public class BookController {

    private final BookService bookService;

    @GetMapping("{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = bookService.findBookById(id);
        return ResponseEntity.ok(book);
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAll() {
        List<Book> books = bookService.findAll();
        return ResponseEntity.ok(books);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Boolean> deleteBookById(@PathVariable Long id) {
        return  ResponseEntity.status(204).body(bookService.deleteBook(id));
    }

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        return ResponseEntity.ok(bookService.createBook(book));
    }

}
