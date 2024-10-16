package com.bookstore.dto.cart;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AddBookCartRequestDto {
    @NotNull
    private Long bookId;

    @NotNull
    @Positive
    private Integer quantity;
}
