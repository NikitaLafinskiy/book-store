package com.bookstore.domain.book.service;

import com.bookstore.domain.book.dto.BookDto;
import com.bookstore.domain.book.dto.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto createBookRequestDto);

    BookDto findById(Long id);

    List<BookDto> findAll(Pageable pageable);

    BookDto updateById(Long id, CreateBookRequestDto createBookRequestDto);

    void deleteById(Long id);

    List<BookDto> getBooksByCategoryIds(Long id, Pageable pageable);
}
