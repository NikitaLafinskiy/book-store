package com.bookstore.service;

import com.bookstore.dto.book.BookDto;
import com.bookstore.dto.book.CreateBookRequestDto;
import com.bookstore.entity.Book;
import com.bookstore.mapper.BookMapper;
import com.bookstore.repository.BookRepository;
import com.bookstore.service.book.BookServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    private final static Long VALID_BOOK_ID = 1L;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("""
        Given a valid request save the book and return a Book DTO
        """)
    public void save_validRequest_returnBookDto() {
        // Given
        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
        Book book = new Book();
        BookDto expected = new BookDto();

        // When
        Mockito.when(bookMapper.toEntity(createBookRequestDto)).thenReturn(book);
        Mockito.when(bookRepository.save(book)).thenReturn(book);
        Mockito.when(bookMapper.toDto(book)).thenReturn(expected);
        BookDto actual = bookService.save(createBookRequestDto);

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(bookRepository, Mockito.times(1)).save(book);
        Mockito.verify(bookMapper, Mockito.times(1)).toEntity(createBookRequestDto);
        Mockito.verify(bookMapper, Mockito.times(1)).toDto(book);
        Mockito.verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("""
        Given a valid book id return a Book DTO
        """)
    public void findById_validId_returnBookDto() {
        // Given
        Book book = new Book(VALID_BOOK_ID);
        BookDto expected = new BookDto();

        // When
        Mockito.when(bookRepository.findById(VALID_BOOK_ID)).thenReturn(Optional.of(book));
        Mockito.when(bookMapper.toDto(book)).thenReturn(expected);
        BookDto actual = bookService.findById(VALID_BOOK_ID);

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(bookRepository, Mockito.times(1)).findById(VALID_BOOK_ID);
        Mockito.verify(bookMapper, Mockito.times(1)).toDto(book);
    }

    @Test
    @DisplayName("""
        Find all books and return a list of Book DTOs
        """)
    public void findAll_validPageable_returnBookDtoList() {
        // Given
        Book firstBook = new Book();
        Book secondBook = new Book();
        List<Book> books = List.of(firstBook, secondBook);
        Page<Book> bookPage = new PageImpl<>(books);
        Pageable pageable = Pageable.ofSize(1);
        BookDto firstBookDto = new BookDto();
        BookDto secondBookDto = new BookDto();
        List<BookDto> expected = List.of(firstBookDto, secondBookDto);

        // When
        Mockito.when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        Mockito.when(bookMapper.toDto(firstBook)).thenReturn(firstBookDto);
        Mockito.when(bookMapper.toDto(secondBook)).thenReturn(secondBookDto);
        List<BookDto> actual = bookService.findAll(pageable);

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(bookRepository, Mockito.times(1)).findAll(pageable);
        Mockito.verify(bookMapper, Mockito.times(1)).toDto(firstBook);
        Mockito.verify(bookMapper, Mockito.times(1)).toDto(secondBook);
    }

    @Test
    @DisplayName("""
        Given a valid book id and request update the book and return a Book DTO
        """)
    public void updateById_validId_returnBookDto() {
        // Given
        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
        Book book = new Book();
        BookDto expected = new BookDto();

        // When
        Mockito.when(bookRepository.findById(VALID_BOOK_ID)).thenReturn(Optional.of(book));
        Mockito.doNothing().when(bookMapper).updateBookFromDto(createBookRequestDto, book);
        Mockito.when(bookRepository.save(book)).thenReturn(book);
        Mockito.when(bookMapper.toDto(book)).thenReturn(expected);
        BookDto actual = bookService.updateById(VALID_BOOK_ID, createBookRequestDto);

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(bookRepository, Mockito.times(1)).findById(VALID_BOOK_ID);
        Mockito.verify(bookMapper, Mockito.times(1)).updateBookFromDto(createBookRequestDto, book);
        Mockito.verify(bookRepository, Mockito.times(1)).save(book);
        Mockito.verify(bookMapper, Mockito.times(1)).toDto(book);
    }

    @Test
    @DisplayName("""
        Given a valid category id and pageable return a list of Book DTOs
        """)
    public void getBooksByCategoryId_validCategoryIdAndPageable_returnBookDtoList() {
        // Given
        Book firstBook = new Book();
        Book secondBook = new Book();
        List<Book> books = List.of(firstBook, secondBook);
        Page<Book> bookPage = new PageImpl<>(books);
        Pageable pageable = Pageable.ofSize(1);
        BookDto firstBookDto = new BookDto();
        BookDto secondBookDto = new BookDto();
        List<BookDto> expected = List.of(firstBookDto, secondBookDto);

        // When
        Mockito.when(bookRepository.findAllByCategoryId(pageable, VALID_BOOK_ID)).thenReturn(bookPage);
        Mockito.when(bookMapper.toDto(firstBook)).thenReturn(firstBookDto);
        Mockito.when(bookMapper.toDto(secondBook)).thenReturn(secondBookDto);
        List<BookDto> actual = bookService.getBooksByCategoryId(VALID_BOOK_ID, pageable);

        // Then
        Assertions.assertEquals(expected, actual);
        Mockito.verify(bookRepository, Mockito.times(1)).findAllByCategoryId(pageable, VALID_BOOK_ID);
        Mockito.verify(bookMapper, Mockito.times(1)).toDto(firstBook);
        Mockito.verify(bookMapper, Mockito.times(1)).toDto(secondBook);
    }
}
