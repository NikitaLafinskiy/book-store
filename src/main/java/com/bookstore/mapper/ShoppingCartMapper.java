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
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(config = MapperConfig.class, uses = { UserMapper.class, CartItemMapper.class })
public abstract class ShoppingCartMapper {
    @Autowired
    private CartItemMapper cartItemMapper;

    @Mapping(target = "cartItems", ignore = true)
    @Mapping(target = "userId", source = "user.id")
    public abstract ShoppingCartDto toDto(ShoppingCart shoppingCart);

    @AfterMapping
    private void setCartItemDtos(ShoppingCart shoppingCart,
                                 @MappingTarget ShoppingCartDto shoppingCartDto) {
        Set<CartItemDto> cartItemDtos = shoppingCart.getCartItems()
                .stream()
                .map(cartItemMapper::toDto)
                .collect(Collectors.toSet());
        shoppingCartDto.setCartItems(cartItemDtos);
    }

    @Mapping(target = "cartItems", ignore = true)
    @Mapping(target = "user", source = "userId", qualifiedByName = "userById")
    public abstract ShoppingCart toEntity(ShoppingCartDto shoppingCartDto);

    @AfterMapping
    private void setCartItems(ShoppingCartDto shoppingCartDto,
                              @MappingTarget ShoppingCart shoppingCart) {
        Set<CartItem> cartItems = shoppingCartDto.getCartItems()
                .stream()
                .map(cartItemMapper::toCartItem)
                .collect(Collectors.toSet());
        shoppingCart.setCartItems(cartItems);
    }
}
