package com.bookstore.service.order;

import com.bookstore.dto.order.OrderItemDto;
import java.util.List;

public interface OrderItemService {
    List<OrderItemDto> getOrderItems(Long orderId, String userId);

    OrderItemDto getOrderItem(Long orderItemId, Long orderId, String userId);
}
