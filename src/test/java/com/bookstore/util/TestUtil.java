package com.bookstore.util;

import com.bookstore.dto.book.BookDto;
import com.bookstore.dto.category.CategoryDto;
import com.bookstore.entity.Book;
import com.bookstore.entity.Category;

import java.math.BigDecimal;
import java.util.List;

public class TestUtil {
    private final static Long CATEGORY_ID = 1L;

    public static List<BookDto> bootstrapBookDtoList() {
        BookDto firstBook = new BookDto();
        firstBook.setId(1L);
        firstBook.setTitle("The Hobbit");
        firstBook.setPrice(new BigDecimal(8.99f));
        firstBook.setAuthor("J.R.R. Tolkien");
        firstBook.setIsbn("978-0547928227");
        firstBook.getCategoryIds().add(CATEGORY_ID);

        BookDto secondBook = new BookDto();
        secondBook.setId(2L);
        secondBook.setTitle("The Lord of the Rings");
        secondBook.setPrice(new BigDecimal(19.99f));
        secondBook.setAuthor("J.R.R. Tolkien");
        secondBook.setIsbn("978-0544003415");
        secondBook.getCategoryIds().add(CATEGORY_ID);

        return List.of(
                firstBook,
                secondBook
        );
    }

    public static List<Book> bootstrapBookList() {
        Category category = bootstrapCategory();

        Book firstBook = new Book();
        firstBook.setId(1L);
        firstBook.setTitle("The Hobbit");
        firstBook.setPrice(new BigDecimal(8.99f));
        firstBook.setAuthor("J.R.R. Tolkien");
        firstBook.setIsbn("978-0547928227");
        firstBook.getCategories().add(category);

        Book secondBook = new Book();
        secondBook.setId(2L);
        secondBook.setTitle("The Lord of the Rings");
        secondBook.setPrice(new BigDecimal(19.99f));
        secondBook.setAuthor("J.R.R. Tolkien");
        secondBook.setIsbn("978-0544003415");
        secondBook.getCategories().add(category);

        return List.of(
                firstBook,
                secondBook
        );
    }

    public static Category bootstrapCategory() {
        Category category = new Category();
        category.setId(CATEGORY_ID);
        category.setDescription("Fantasy is a genre of "
                + "speculative fiction set in a fictional universe, "
                + "often inspired by real world myth and folklore.");
        category.setName("Fantasy");

        return category;
    }

    public static CategoryDto bootstrapCategoryDto() {
        CategoryDto category = new CategoryDto();
        category.setId(CATEGORY_ID);
        category.setDescription("Fantasy is a genre of "
                + "speculative fiction set in a fictional universe, "
                + "often inspired by real world myth and folklore.");
        category.setName("Fantasy");

        return category;
    }

    public static boolean compareTruncatedRoundedDecimals(BigDecimal expected, BigDecimal actual) {
        return Math.round(Math.floor(expected.doubleValue() * 100) / 100) == Math.round(Math.floor(actual.doubleValue() * 100) / 100);
    }
}
