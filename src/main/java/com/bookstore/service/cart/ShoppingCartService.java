package com.bookstore.service.cart;

import com.bookstore.dto.cart.AddBookCartRequestDto;
import com.bookstore.dto.cart.ShoppingCartDto;
import com.bookstore.dto.cart.UpdateCartRequestDto;
import com.bookstore.entity.ShoppingCart;
import com.bookstore.entity.User;

public interface ShoppingCartService {
    ShoppingCartDto getCart(String id);

    ShoppingCartDto addCartItem(String id, AddBookCartRequestDto addBookCartRequestDto);

    ShoppingCartDto updateCartItem(String id,
                                   UpdateCartRequestDto updateCartRequestDto,
                                   Long cartItemId);

    void deleteCartItem(Long cartItemId);

    ShoppingCart createShoppingCart(User user);
}
