package com.bookstore.dto.order;

import com.bookstore.entity.Order;
import lombok.Data;

@Data
public class UpdateOrderStatusRequestDto {
    private Order.Status status;
}
