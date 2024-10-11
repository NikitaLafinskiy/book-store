package com.bookstore.domain.category.mapper;

import com.bookstore.config.MapperConfig;
import com.bookstore.domain.category.dto.CategoryDto;
import com.bookstore.domain.category.dto.CreateCategoryRequestDto;
import com.bookstore.domain.category.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toEntity(CategoryDto categoryDto);

    Category toEntity(CreateCategoryRequestDto categoryDto);

    void updateCategoryFromDto(CategoryDto categoryDto,
                               @MappingTarget Category category);

    void updateCategoryFromDto(CreateCategoryRequestDto categoryDto,
                               @MappingTarget Category category);
}
