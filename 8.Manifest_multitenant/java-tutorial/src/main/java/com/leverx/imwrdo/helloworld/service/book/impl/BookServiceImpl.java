package com.leverx.imwrdo.helloworld.service.book.impl;

import com.leverx.imwrdo.helloworld.model.Book;
import com.leverx.imwrdo.helloworld.repository.BookRepository;
import com.leverx.imwrdo.helloworld.service.book.BookService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing books.
 */
@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    /**
     * Retrieves all books from the repository.
     *
     * @return a list of all books
     */
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    /**
     * Inserts a new book into the repository.
     *
     * @param book the book to be inserted
     * @return Book if the insertion was successful
     */
    public Book insertBook(Book book) {
        bookRepository.save(book);
        return book;
    }

    /**
     * Retrieves a book by its ID.
     *
     * @param id the ID of the book to be retrieved
     * @return the book with the specified ID
     */
    public Book findBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
    }

    /**
     * Deletes a book by its ID.
     *
     * @param id the ID of the book to be deleted
     * @return true if the deletion was successful
     */
    public boolean deleteBook(long id) {
        Optional<Book> book = bookRepository.findById(id);
        book.ifPresent(bookRepository::delete);
        return true;
    }


}
