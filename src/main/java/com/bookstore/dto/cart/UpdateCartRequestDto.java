package com.bookstore.dto.cart;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpdateCartRequestDto {
    @Positive
    private int quantity;
}
