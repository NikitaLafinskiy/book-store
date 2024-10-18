package com.bookstore.mapper;

import com.bookstore.config.MapperConfig;
import com.bookstore.dto.cart.CartItemDto;
import com.bookstore.dto.cart.ShoppingCartDto;
import com.bookstore.entity.CartItem;
import com.bookstore.entity.ShoppingCart;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = { UserMapper.class, BookMapper.class })
public interface ShoppingCartMapper {
    @Mapping(target = "cartItems", ignore = true)
    @Mapping(target = "userId", source = "user.id")
    ShoppingCartDto toDto(ShoppingCart shoppingCart);

    @AfterMapping
    default void setCartItemDtos(ShoppingCart shoppingCart,
                                 @MappingTarget ShoppingCartDto shoppingCartDto) {
        Set<CartItemDto> cartItemDtos = shoppingCart.getCartItems()
                .stream()
                .map(this::toCartItemDto)
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
                .map(this::toCartItem)
                .collect(Collectors.toSet());
        shoppingCart.setCartItems(cartItems);
    }

    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    CartItemDto toCartItemDto(CartItem cartItem);

    @Mapping(target = "book", source = "bookId", qualifiedByName = "bookById")
    CartItem toCartItem(CartItemDto cartItemDto);
}
