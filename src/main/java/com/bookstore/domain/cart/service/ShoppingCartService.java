package com.bookstore.domain.cart.service;

import com.bookstore.domain.cart.dto.AddBookCartRequestDto;
import com.bookstore.domain.cart.dto.CartItemDto;
import com.bookstore.domain.cart.dto.ShoppingCartDto;
import com.bookstore.domain.cart.dto.UpdateCartRequestDto;

public interface ShoppingCartService {
    ShoppingCartDto getCart(String email);

    ShoppingCartDto addCartItem(String email, AddBookCartRequestDto addBookCartRequestDto);

    CartItemDto updateCartItem(UpdateCartRequestDto updateCartRequestDto,
                               Long cartItemId);

    void deleteCartItem(Long cartItemId);
}
