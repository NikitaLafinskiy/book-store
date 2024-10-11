package com.bookstore.domain.cart.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateCartRequestDto {
    @NotNull
    @Max(100)
    @Min(0)
    private Integer quantity;
}
