package com.bookstore.domain.order.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateOrderItemRequestDto {
    @NotNull
    private Long bookId;

    @NotNull
    @Min(1)
    @Max(999)
    private int quantity;

    @NotNull
    private BigDecimal price;
}
