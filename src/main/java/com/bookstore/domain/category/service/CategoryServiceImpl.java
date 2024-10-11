package com.bookstore.domain.category.service;

import com.bookstore.domain.category.dto.CategoryDto;
import com.bookstore.domain.category.dto.CreateCategoryRequestDto;
import com.bookstore.domain.category.entity.Category;
import com.bookstore.domain.category.mapper.CategoryMapper;
import com.bookstore.domain.category.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryDto findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryDto save(CreateCategoryRequestDto categoryDto) {
        return categoryMapper.toDto(categoryRepository.save(categoryMapper.toEntity(categoryDto)));
    }

    @Override
    public CategoryDto update(Long id, CreateCategoryRequestDto categoryDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        categoryMapper.updateCategoryFromDto(categoryDto, category);
        category = categoryRepository.save(category);
        return categoryMapper.toDto(category);
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}
