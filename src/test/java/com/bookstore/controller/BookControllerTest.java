package com.bookstore.controller;

import static com.bookstore.util.TestUtil.bootstrapBookDtoList;
import static com.bookstore.util.TestUtil.compareTruncatedRoundedDecimals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.bookstore.dto.book.BookDto;
import com.bookstore.dto.book.CreateBookRequestDto;
import com.bookstore.exception.EntityNotFoundException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@Sql(scripts = {"classpath:/database/books/delete-all-books-and-categories.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class BookControllerTest {
    private static final String VALID_BOOK_AUTHOR = "Author";
    private static final String VALID_BOOK_TITLE = "Title";
    private static final String VALID_BOOK_ISBN = "978-0-439-02348-1";
    private static final BigDecimal VALID_BOOK_PRICE = new BigDecimal(100);

    private static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext context) {
        mockMvc = webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("""
            Given a valid request to save a book
            When POST request is sent to /api/books
            Then return a Book Dto
            """)
    @WithMockUser(username = "admin@bookstore.com", roles = {"ADMIN"})
    public void save_validRequest_returnBookDto() throws Exception {
        // Given
        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto()
                .setIsbn(VALID_BOOK_ISBN)
                .setAuthor(VALID_BOOK_AUTHOR)
                .setTitle(VALID_BOOK_TITLE)
                .setPrice(VALID_BOOK_PRICE);
        BookDto expected = new BookDto()
                .setIsbn(VALID_BOOK_ISBN)
                .setAuthor(VALID_BOOK_AUTHOR)
                .setTitle(VALID_BOOK_TITLE)
                .setPrice(VALID_BOOK_PRICE);
        String jsonRequest = objectMapper.writeValueAsString(createBookRequestDto);

        // When
        MvcResult result = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();
        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookDto.class);

        // Then
        assertTrue(EqualsBuilder.reflectionEquals(expected, actual,
                "categoryIds", "coverImage", "id", "description"));
    }

    @Test
    @DisplayName("""
            Given a valid role
            When a GET request is sent to /api/books/
            Then return a list of Book Dtos
            """)
    @WithMockUser(username = "username")
    @Sql(scripts = {"classpath:/database/books/add-one-category.sql",
            "classpath:/database/books/add-two-books-with-categories.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAll_validRole_returnListOfBookDtos() throws Exception {
        // Given
        List<BookDto> expected = bootstrapBookDtoList();

        // When
        MvcResult result = mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        List<BookDto> actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<>() {
                });
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < actual.size(); i++) {
            BookDto actualBook = actual.get(i);
            BookDto expectedBook = expected.get(i);
            assertTrue(EqualsBuilder.reflectionEquals(expectedBook, actualBook, "categoryIds",
                    "coverImage",
                    "id",
                    "description",
                    "price"));
            assertTrue(compareTruncatedRoundedDecimals(expectedBook.getPrice(),
                    actualBook.getPrice()));
            assertEquals(expectedBook.getCategoryIds().size(), actualBook.getCategoryIds().size());
        }
    }

    @WithMockUser(username = "user")
    @Test
    @DisplayName("""
            Given a negative id
            When a GET request is sent to /api/books/{id}
            Then throw an exception
            """)
    void getBookById_negativeId_throwException() throws Exception {
        // Given
        Long invalidId = -1L;

        // When
        mockMvc.perform(get("/books/{id}", invalidId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertInstanceOf(EntityNotFoundException.class,
                        result.getResolvedException()));

        // Then
    }

    @Test
    @DisplayName(
            """
            Given an invalid book dto
            When a POST request is sent to /api/books
            Then return a 400 Bad Request
            """
    )
    @WithMockUser(username = "user", roles = {"ADMIN"})
    public void save_invalidRequestDto_returnBadRequest() throws Exception {
        // Given
        String invalidIsbn = "99999999";
        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto()
                .setIsbn(invalidIsbn)
                .setAuthor(VALID_BOOK_AUTHOR)
                .setTitle(VALID_BOOK_TITLE)
                .setPrice(VALID_BOOK_PRICE);
        String jsonRequest = objectMapper.writeValueAsString(createBookRequestDto);

        // When
        mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());

        // Then
    }
}
