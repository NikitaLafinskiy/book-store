package com.bookstore.domain.category.service;

import com.bookstore.domain.category.dto.CategoryDto;
import com.bookstore.domain.category.dto.CreateCategoryRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    List<CategoryDto> findAll(Pageable pageable);

    CategoryDto findById(Long id);

    CategoryDto save(CreateCategoryRequestDto categoryDto);

    CategoryDto update(Long id, CreateCategoryRequestDto categoryDto);

    void deleteById(Long id);
}
