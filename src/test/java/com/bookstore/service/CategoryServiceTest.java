package com.bookstore.service;

import com.bookstore.dto.category.CategoryDto;
import com.bookstore.dto.category.CreateCategoryRequestDto;
import com.bookstore.entity.Category;
import com.bookstore.mapper.CategoryMapper;
import com.bookstore.repository.CategoryRepository;
import com.bookstore.service.category.CategoryServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("""
            Given nothing
            When findAll method is called
            Then return a list of categories
            """)
    public void finAll_called_returnListOfCategories() {
        // Given
        List<CategoryDto> expected = List.of(new CategoryDto(), new CategoryDto());
        List<Category> categories = List.of(new Category(), new Category());

        // When
        when(categoryRepository.findAll(Pageable.unpaged())).thenReturn(new PageImpl<>(categories));
        for (int i = 0; i < categories.size(); i++) {
            when(categoryMapper.toDto(categories.get(i))).thenReturn(expected.get(i));
        }
        List<CategoryDto> actual = categoryService.findAll(Pageable.unpaged());

        // Then
        assertEquals(expected.size(), actual.size());
        verify(categoryRepository, times(1)).findAll(Pageable.unpaged());
        for (Category category : categories) {
            verify(categoryMapper, times(1)).toDto(category);
        }
    }

    @Test
    @DisplayName("""
            Given a category id
            When findById method is called
            Then return a category
            """)
    public void findById_validId_returnCategory() {
        // Given
        Long id = 1L;
        CategoryDto expected = new CategoryDto();
        expected.setId(id);
        Category category = new Category();
        category.setId(id);

        // When
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(expected);
        CategoryDto actual = categoryService.findById(id);

        // Then
        assertEquals(expected.getId(), actual.getId());
        verify(categoryRepository, times(1)).findById(id);
        verify(categoryMapper, times(1)).toDto(category);
    }

    @Test
    @DisplayName("""
            Given a category
            When save method is called
            Then return a category
            """)
    public void save_validCreateRequestDto_returnCategory() {
        // Given
        Long id = 1L;
        CategoryDto expected = new CategoryDto();
        expected.setId(id);
        CreateCategoryRequestDto createCategoryRequestDto = new CreateCategoryRequestDto();
        Category category = new Category();
        category.setId(id);

        // When
        when(categoryMapper.toEntity(createCategoryRequestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(expected);
        CategoryDto actual = categoryService.save(createCategoryRequestDto);

        // Then
        assertEquals(expected.getId(), actual.getId());
        verify(categoryMapper, times(1)).toEntity(createCategoryRequestDto);
        verify(categoryRepository, times(1)).save(category);
        verify(categoryMapper, times(1)).toDto(category);
    }
}
