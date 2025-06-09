package com.example.java.tutorial.service;

import com.example.java.tutorial.model.Book;

import java.util.List;

/**
 * Service interface for managing books.
 */
public interface BookService {
    List<Book> findAll();

    Book createBook(Book book);

    Book findBookById(Long id);

    boolean deleteBook(Long id);
}
