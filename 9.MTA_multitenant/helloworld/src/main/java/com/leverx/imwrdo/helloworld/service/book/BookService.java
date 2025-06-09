package com.leverx.imwrdo.helloworld.service.book;

import com.leverx.imwrdo.helloworld.model.Book;

import java.util.List;

/**
 * Service interface for managing books.
 */
public interface BookService {
    List<Book> findAll();

    Book insertBook(Book book);

    Book findBookById(Long id);

    boolean deleteBook(long id);
}