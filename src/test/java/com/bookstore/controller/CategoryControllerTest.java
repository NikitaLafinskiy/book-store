package com.bookstore.controller;

import static com.bookstore.util.TestUtil.bootstrapBookDtoList;
import static com.bookstore.util.TestUtil.bootstrapCategoryDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.bookstore.dto.book.BookDto;
import com.bookstore.dto.category.CategoryDto;
import com.bookstore.dto.category.CreateCategoryRequestDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.RoundingMode;
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

@SpringBootTest
@Testcontainers
@Sql(scripts = {"classpath:/database/books/add-one-category.sql",
                "classpath:/database/books/add-two-books-with-categories.sql"})
@Sql(scripts = {"classpath:/database/books/delete-all-books-and-categories.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CategoryControllerTest {
    private static final int BIG_DECIMAL_SCALE = 1;
    private static final int BIG_DECIMAL_EQUAL = 0;

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
    @DisplayName(
            """
            Given nothing
            When findAll method is called
            Then return a list of category dtos
            """
    )
    @WithMockUser(username = "user")
    public void findAll_validRequest_returnListOfCategoryDtos() throws Exception {
        // Given
        CategoryDto categoryDto = bootstrapCategoryDto();
        List<CategoryDto> expected = List.of(categoryDto);

        // When
        MvcResult result = mockMvc.perform(get("/categories")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        List<CategoryDto> actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<>() {
                });

        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(0).getName(), actual.get(0).getName());
        assertEquals(expected.get(0).getDescription(), actual.get(0).getDescription());
    }

    @Test
    @DisplayName(
            """
            Given a category request dto
            When createCategory method is called
            Then return a category dto
            """
    )
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void createCategory_validRequest_returnCategoryDto() throws Exception {
        // Given
        CategoryDto expected = bootstrapCategoryDto();
        CreateCategoryRequestDto createCategoryRequestDto = new CreateCategoryRequestDto()
                .setName(expected.getName())
                .setDescription(expected.getDescription());
        String jsonRequest = objectMapper.writeValueAsString(createCategoryRequestDto);

        // When
        MvcResult result = mockMvc.perform(post("/categories")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        CategoryDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                CategoryDto.class);

        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
    }

    @Test
    @DisplayName(
            """
            Given an invalid category id
            When getCategoryById method is called
            Then return a bad request status
            """
    )
    @WithMockUser(username = "user")
    public void getCategoryById_invalidId_returnNotFound() throws Exception {
        // Given
        long invalidId = -1L;

        // When
        mockMvc.perform(get("/categories/" + invalidId)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName(
            """
            Given a valid category ID
            When getBooksByCategoryId method is called
            Then return a list of book dtos
            """
    )
    @WithMockUser(username = "user")
    public void getBookByCategoryId_validId_returnListOfBookDtos() throws Exception {
        // Given
        long categoryId = 1L;
        List<BookDto> expected = bootstrapBookDtoList();

        // When
        MvcResult result = mockMvc.perform(get("/categories/" + categoryId + "/books")
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
            assertEquals(BIG_DECIMAL_EQUAL, expectedBook.getPrice()
                    .setScale(BIG_DECIMAL_SCALE, RoundingMode.FLOOR)
                    .compareTo(actualBook.getPrice()
                            .setScale(BIG_DECIMAL_SCALE, RoundingMode.FLOOR)));
            assertEquals(expectedBook.getCategoryIds().size(), actualBook.getCategoryIds().size());
        }
    }
}
