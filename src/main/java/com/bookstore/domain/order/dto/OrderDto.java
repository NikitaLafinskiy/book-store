package com.bookstore.domain.order.dto;

import com.bookstore.domain.order.entity.Order;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;

@Data
public class OrderDto {
    private Long id;
    private Long userId;
    private Order.Status status;
    private BigDecimal total;
    private LocalDateTime orderDate;
    private Set<OrderItemDto> orderItems;
}
