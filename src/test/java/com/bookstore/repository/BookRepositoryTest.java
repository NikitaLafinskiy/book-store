package com.bookstore.repository;

import static com.bookstore.util.TestUtil.bootstrapBookList;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.bookstore.config.BookstoreMySqlContainer;
import com.bookstore.entity.Book;
import java.math.RoundingMode;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@Sql(scripts = {
        "classpath:database/books/add-one-category.sql",
        "classpath:database/books/add-two-books-with-categories.sql"
})
@Sql(scripts = {
        "classpath:database/books/delete-all-books-and-categories.sql"
}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class BookRepositoryTest {
    private static final int BIG_DECIMAL_SCALE = 1;
    private static final int BIG_DECIMAL_EQUAL = 0;
    private static final Long CATEGORY_ID = 1L;

    static {
        BookstoreMySqlContainer.getInstance();
    }

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
        List<Book> booksList = bootstrapBookList();

        // When
        Page<Book> result = bookRepository.findAllByCategoryId(Pageable.unpaged(), CATEGORY_ID);

        // Then
        List<Book> actual = result.getContent();
        assertEquals(actual.size(), 2);
        for (int i = 0; i < actual.size(); i++) {
            Book actualBook = actual.get(i);
            Book expectedBook = booksList.get(i);
            assertEquals(expectedBook.getTitle(), actualBook.getTitle());
            assertEquals(BIG_DECIMAL_EQUAL, expectedBook.getPrice()
                    .setScale(BIG_DECIMAL_SCALE, RoundingMode.FLOOR)
                    .compareTo(actualBook.getPrice()
                            .setScale(BIG_DECIMAL_SCALE, RoundingMode.FLOOR)));
            assertEquals(expectedBook.getAuthor(), actualBook.getAuthor());
            assertEquals(expectedBook.getIsbn(), actualBook.getIsbn());
            assertEquals(expectedBook.getCategories().size(), actualBook.getCategories().size());
        }
    }
}
