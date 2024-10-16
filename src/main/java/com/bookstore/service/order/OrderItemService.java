package com.bookstore.service.order;

import com.bookstore.dto.order.OrderItemDto;
import java.util.List;

public interface OrderItemService {
    List<OrderItemDto> getOrderItems(Long orderId);

    OrderItemDto getOrderItem(Long orderItemId);
}
