package com.bookstore.repository;

import com.bookstore.config.BookstoreMySqlContainer;
import com.bookstore.entity.Book;
import com.bookstore.util.TestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@Sql(scripts = {
        "classpath:database/books/add-one-category.sql",
        "classpath:database/books/add-two-books-with-categories.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {
        "classpath:database/books/delete-all-books-and-categories.sql"
}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class BookRepositoryTest {
    private static final Long CATEGORY_ID = 1L;

    @Container
    private static final BookstoreMySqlContainer container = BookstoreMySqlContainer.getInstance();

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("""
            Given two books in the database
            When findAllByCategoryId() is invoked
            Then it should return all books associated to a given category
            """)
    public void findAllByCategoryId_twoBooksInDatabase_returnAll() {
        // Given
        List<Book> booksList = TestUtil.bootstrapBookList();

        // When
        Page<Book> result = bookRepository.findAllByCategoryId(Pageable.unpaged(), CATEGORY_ID);

        // Then
        List<Book> actual = result.getContent();
        Assertions.assertEquals(actual.size(), 2);
        for (int i = 0; i < actual.size(); i++) {
            Book actualBook = actual.get(i);
            Book expectedBook = booksList.get(i);
            Assertions.assertEquals(expectedBook.getTitle(), actualBook.getTitle());
            Assertions.assertTrue(TestUtil.compareTruncatedRoundedDecimals(expectedBook.getPrice(), actualBook.getPrice()));
            Assertions.assertEquals(expectedBook.getAuthor(), actualBook.getAuthor());
            Assertions.assertEquals(expectedBook.getIsbn(), actualBook.getIsbn());
            Assertions.assertEquals(expectedBook.getCategories().size(), actualBook.getCategories().size());
        }
    }
}
