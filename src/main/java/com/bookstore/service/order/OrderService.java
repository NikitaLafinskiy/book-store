package com.bookstore.service.order;

import com.bookstore.dto.order.CreateOrderRequestDto;
import com.bookstore.dto.order.OrderDto;
import com.bookstore.dto.order.UpdateOrderStatusRequestDto;
import java.util.List;

public interface OrderService {
    OrderDto createOrder(String email, CreateOrderRequestDto createOrderRequestDto);

    List<OrderDto> getOrderHistory(String email);

    OrderDto updateOrderStatus(Long orderId,
                               UpdateOrderStatusRequestDto updateOrderStatusRequestDto);
}
