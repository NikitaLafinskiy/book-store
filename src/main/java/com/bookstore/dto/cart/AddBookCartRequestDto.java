package com.bookstore.dto.cart;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddBookCartRequestDto {
    @NotNull
    private Long bookId;

    @NotNull
    @Min(0)
    @Max(100)
    private Integer quantity;
}
