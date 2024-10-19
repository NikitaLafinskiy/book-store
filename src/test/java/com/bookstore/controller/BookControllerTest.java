package com.bookstore.controller;

import com.bookstore.dto.book.BookDto;
import com.bookstore.dto.book.CreateBookRequestDto;
import lombok.SneakyThrows;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class BookControllerTest {
    private static final String VALID_BOOK_AUTHOR = "Author";
    private static final String VALID_BOOK_TITLE = "Title";
    private static final String VALID_BOOK_ISBN = "978-0-439-02348-1";
    private static final BigDecimal VALID_BOOK_PRICE = new BigDecimal(100);

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    @Test
    @DisplayName("""
        Given a valid request to save a book
        When POST request is sent to /api/books
        Then return a Book Dto
        """)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void save_validRequest_returnBookDto() {
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
        MvcResult result = mockMvc.perform(post(
                "/api/books"
        ).content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andReturn();
        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookDto.class);

        // Then
        assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
    }
}
