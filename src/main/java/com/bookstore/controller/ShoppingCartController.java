package com.bookstore.controller;

import com.bookstore.dto.cart.AddBookCartRequestDto;
import com.bookstore.dto.cart.ShoppingCartDto;
import com.bookstore.dto.cart.UpdateCartRequestDto;
import com.bookstore.service.cart.ShoppingCartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ShoppingCartDto getCart(@AuthenticationPrincipal String id) {
        return shoppingCartService.getCart(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ShoppingCartDto addCartItem(
            @AuthenticationPrincipal String id,
            @RequestBody @Valid AddBookCartRequestDto addBookCartRequestDto) {
        return shoppingCartService.addCartItem(id, addBookCartRequestDto);
    }

    @PutMapping("/items/{cartItemId}")
    @PreAuthorize("hasRole('USER')")
    public ShoppingCartDto updateCartItem(
            @RequestBody @Valid UpdateCartRequestDto updateCartRequestDto,
            @AuthenticationPrincipal String id,
            @PathVariable Long cartItemId) {
        return shoppingCartService.updateCartItem(id,
                updateCartRequestDto,
                cartItemId);
    }

    @DeleteMapping("/items/{cartItemId}")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCartItem(@PathVariable Long cartItemId) {
        shoppingCartService.deleteCartItem(cartItemId);
    }
}
