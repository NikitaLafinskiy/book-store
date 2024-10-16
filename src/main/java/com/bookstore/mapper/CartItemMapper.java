package com.bookstore.mapper;

import com.bookstore.config.MapperConfig;
import com.bookstore.dto.cart.AddBookCartRequestDto;
import com.bookstore.dto.cart.CartItemDto;
import com.bookstore.dto.cart.UpdateCartRequestDto;
import com.bookstore.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = { BookMapper.class })
public interface CartItemMapper {
    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    CartItemDto toDto(CartItem cartItem);

    @Mapping(target = "book", source = "bookId", qualifiedByName = "bookById")
    CartItem toCartItem(CartItemDto cartItemDto);

    @Mapping(target = "book", source = "bookId", qualifiedByName = "bookById")
    CartItem toCartItem(AddBookCartRequestDto addBookCartRequestDto);

    void updateCartItem(UpdateCartRequestDto updateCartRequestDto,
                        @MappingTarget CartItem cartItem);
}
