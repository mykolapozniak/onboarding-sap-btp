package com.leverx.imwrdo.helloworld.controller;

import com.leverx.imwrdo.helloworld.model.Book;
import com.leverx.imwrdo.helloworld.service.book.BookService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * BookController is a REST controller that handles requests related to books.
 * It provides endpoints for managing book resources.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;

    /**
     * Retrieves a book by its ID.
     *
     * @param id the ID of the book to retrieve
     * @return a ResponseEntity containing the book and HTTP status
     */
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = bookService.findBookById(id);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    /**
     * Retrieves all books.
     *
     * @return a ResponseEntity containing a list of all books and HTTP status
     */
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.findAll();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    /**
     * Deletes a book by its ID.
     *
     * @param id the ID of the book to delete
     * @return a ResponseEntity containing a boolean indicating success and HTTP status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteBookById(@PathVariable Long id) {
        return new ResponseEntity<>(bookService.deleteBook(id), HttpStatus.OK);
    }

    /**
     * Inserts a new book.
     *
     * @param book the book to insert
     * @return a ResponseEntity containing a boolean indicating success and HTTP status
     */
    @PostMapping
    public ResponseEntity<Book> insertBook(@RequestBody Book book) {
        return new ResponseEntity<>(bookService.insertBook(book), HttpStatus.CREATED);
    }

}
