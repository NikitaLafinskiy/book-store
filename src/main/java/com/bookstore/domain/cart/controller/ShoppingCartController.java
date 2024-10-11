package com.bookstore.domain.cart.controller;

import com.bookstore.domain.cart.dto.AddBookCartRequestDto;
import com.bookstore.domain.cart.dto.CartItemDto;
import com.bookstore.domain.cart.dto.ShoppingCartDto;
import com.bookstore.domain.cart.dto.UpdateCartRequestDto;
import com.bookstore.domain.cart.service.ShoppingCartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ShoppingCartDto getCart(@AuthenticationPrincipal String email) {
        return shoppingCartService.getCart(email);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ShoppingCartDto addCartItem(@AuthenticationPrincipal String email,
                                       @RequestBody @Valid
                                       AddBookCartRequestDto addBookCartRequestDto) {
        return shoppingCartService.addCartItem(email, addBookCartRequestDto);
    }

    @PutMapping("/items/{cartItemId}")
    @PreAuthorize("hasRole('USER')")
    public CartItemDto updateCartItem(@RequestBody @Valid
                                          UpdateCartRequestDto updateCartRequestDto,
                                      @PathVariable Long cartItemId) {
        return shoppingCartService.updateCartItem(updateCartRequestDto,
                cartItemId);
    }

    @DeleteMapping("/items/{cartItemId}")
    @PreAuthorize("hasRole('USER')")
    public void deleteCartItem(@PathVariable Long cartItemId) {
        shoppingCartService.deleteCartItem(cartItemId);
    }
}
