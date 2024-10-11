package com.bookstore.domain.book.mapper;

import com.bookstore.config.MapperConfig;
import com.bookstore.domain.book.dto.BookDto;
import com.bookstore.domain.book.dto.BookDtoWithoutCategoryIds;
import com.bookstore.domain.book.dto.CreateBookRequestDto;
import com.bookstore.domain.book.entity.Book;
import com.bookstore.domain.category.entity.Category;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    @Mapping(target = "categoryIds", ignore = true)
    BookDto toDto(Book book);

    @AfterMapping
    default void setCategoryIds(Book book, @MappingTarget BookDto bookDto) {
        Set<Long> categoryIds = book.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toSet());
        bookDto.setCategoryIds(categoryIds);
    }

    @Mapping(target = "categories", ignore = true)
    Book toEntity(CreateBookRequestDto createBookRequestDto);

    @AfterMapping
    default void setCategories(CreateBookRequestDto createBookRequestDto,
                               @MappingTarget Book book) {
        Set<Category> categories = createBookRequestDto.getCategoryIds().stream()
                .map(Category::new)
                .collect(Collectors.toSet());
        book.setCategories(categories);
    }

    void updateBookFromDto(CreateBookRequestDto createBookRequestDto, @MappingTarget Book book);

    BookDtoWithoutCategoryIds toDtoWithoutCategoryIds(Book book);

    @Named("bookById")
    default Book bookById(Long bookId) {
        return Optional.ofNullable(bookId)
                .stream()
                .map(Book::new)
                .findFirst()
                .orElse(null);
    }
}
