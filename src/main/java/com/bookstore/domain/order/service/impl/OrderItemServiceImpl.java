package com.bookstore.domain.order.service.impl;

import com.bookstore.domain.order.dto.OrderItemDto;
import com.bookstore.domain.order.service.OrderItemService;
import java.util.List;

public class OrderItemServiceImpl implements OrderItemService {
    @Override
    public List<OrderItemDto> getOrderItems(Long orderId) {
        return List.of();
    }

    @Override
    public OrderItemDto getOrderItem(Long orderId, Long orderItemId) {
        return null;
    }
}
