package com.bookstore.domain.cart.mapper;

import com.bookstore.config.MapperConfig;
import com.bookstore.domain.book.mapper.BookMapper;
import com.bookstore.domain.cart.dto.AddBookCartRequestDto;
import com.bookstore.domain.cart.dto.CartItemDto;
import com.bookstore.domain.cart.dto.ShoppingCartDto;
import com.bookstore.domain.cart.dto.UpdateCartRequestDto;
import com.bookstore.domain.cart.entity.CartItem;
import com.bookstore.domain.cart.entity.ShoppingCart;
import com.bookstore.domain.user.mapper.UserMapper;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = { UserMapper.class, BookMapper.class })
public interface ShoppingCartMapper {

    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    CartItemDto cartItemToDto(CartItem cartItem);

    @Mapping(target = "book", source = "bookId", qualifiedByName = "bookById")
    CartItem dtoToCartItem(CartItemDto cartItemDto);

    @Mapping(target = "book", source = "bookId", qualifiedByName = "bookById")
    CartItem dtoToCartItem(AddBookCartRequestDto addBookCartRequestDto);

    void updateCartItem(UpdateCartRequestDto updateCartRequestDto,
                        @MappingTarget CartItem cartItem);

    @Mapping(target = "cartItems", ignore = true)
    @Mapping(target = "userId", source = "user.id")
    ShoppingCartDto toDto(ShoppingCart shoppingCart);

    @AfterMapping
    default void setCartItemDtos(ShoppingCart shoppingCart,
                                 @MappingTarget ShoppingCartDto shoppingCartDto) {
        Set<CartItemDto> cartItemDtos = shoppingCart.getCartItems()
                .stream()
                .map(this::cartItemToDto)
                .collect(Collectors.toSet());
        shoppingCartDto.setCartItems(cartItemDtos);
    }

    @Mapping(target = "cartItems", ignore = true)
    @Mapping(target = "user", source = "userId", qualifiedByName = "userById")
    ShoppingCart toEntity(ShoppingCartDto shoppingCartDto);

    @AfterMapping
    default void setCartItems(ShoppingCartDto shoppingCartDto,
                              @MappingTarget ShoppingCart shoppingCart) {
        Set<CartItem> cartItems = shoppingCartDto.getCartItems()
                .stream()
                .map(this::dtoToCartItem)
                .collect(Collectors.toSet());
        shoppingCart.setCartItems(cartItems);
    }
}
