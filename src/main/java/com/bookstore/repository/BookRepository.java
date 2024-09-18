package com.bookstore.repository;

import com.bookstore.entity.Book;
import java.util.List;

public interface BookRepository {
    Book save(Book book);

    Book getById(Long id);

    List<Book> findAll();
}
