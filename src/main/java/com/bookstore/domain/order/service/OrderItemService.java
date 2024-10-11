package com.bookstore.domain.order.service;

import com.bookstore.domain.order.dto.OrderItemDto;
import java.util.List;

public interface OrderItemService {
    List<OrderItemDto> getOrderItems(Long orderId);

    OrderItemDto getOrderItem(Long orderId, Long orderItemId);
}
