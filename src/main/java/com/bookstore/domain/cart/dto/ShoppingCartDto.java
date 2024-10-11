package com.bookstore.domain.cart.dto;

import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ShoppingCartDto {
    private Long id;
    private Long userId;
    private Set<CartItemDto> cartItems = new HashSet<>();

    public ShoppingCartDto(Long userId) {
        this.userId = userId;
    }
}
