package com.bookstore.service.order;

import com.bookstore.dto.order.CreateOrderRequestDto;
import com.bookstore.dto.order.OrderDto;
import com.bookstore.dto.order.OrderItemDto;
import com.bookstore.dto.order.UpdateOrderStatusRequestDto;
import java.util.List;

public interface OrderService {
    OrderDto createOrder(Long userId, CreateOrderRequestDto createOrderRequestDto);

    List<OrderDto> getOrderHistory(Long userId);

    OrderDto updateOrderStatus(Long orderId,
                               UpdateOrderStatusRequestDto updateOrderStatusRequestDto);

    List<OrderItemDto> getOrderItems(Long orderId, Long userId);

    OrderItemDto getOrderItem(Long orderItemId, Long orderId, Long userId);
}
