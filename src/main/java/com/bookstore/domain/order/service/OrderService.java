package com.bookstore.domain.order.service;

import com.bookstore.domain.order.dto.CreateOrderRequestDto;
import com.bookstore.domain.order.dto.OrderDto;
import com.bookstore.domain.order.dto.UpdateOrderStatusRequestDto;
import java.util.List;

public interface OrderService {
    OrderDto createOrder(CreateOrderRequestDto createOrderRequestDto);

    List<OrderDto> getOrderHistory(String email);

    OrderDto updateOrderStatus(Long orderId,
                               UpdateOrderStatusRequestDto updateOrderStatusRequestDto);
}
