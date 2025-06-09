package com.leverx.imwrdo.helloworld.repository;

import com.leverx.imwrdo.helloworld.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    // This interface will automatically provide CRUD operations for the Book entity
    // You can add custom query methods here if needed
}