package com.bookstore.dto.order;

import com.bookstore.entity.Order;
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
