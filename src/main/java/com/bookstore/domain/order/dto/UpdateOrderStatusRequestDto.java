package com.bookstore.domain.order.dto;

import com.bookstore.domain.order.entity.Order;
import lombok.Data;

@Data
public class UpdateOrderStatusRequestDto {
    private Order.Status status;
}
