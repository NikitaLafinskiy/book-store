package com.bookstore.domain.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;

@Data
public class CreateOrderRequestDto {
    @NotBlank
    @Size(min = 5, max = 255)
    private String shippingAddress;

    @NotNull
    private Set<CreateOrderItemRequestDto> orderItems = new HashSet<>();
}
