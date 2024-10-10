package com.bookstore.service.cart;

import com.bookstore.dto.cart.AddBookCartRequestDto;
import com.bookstore.dto.cart.CartItemDto;
import com.bookstore.dto.cart.ShoppingCartDto;
import com.bookstore.dto.cart.UpdateCartRequestDto;

public interface ShoppingCartService {
    ShoppingCartDto getCart(String email);

    ShoppingCartDto addCartItem(String email, AddBookCartRequestDto addBookCartRequestDto);

    CartItemDto updateCartItem(UpdateCartRequestDto updateCartRequestDto,
                               Long cartItemId);

    void deleteCartItem(Long cartItemId);
}
