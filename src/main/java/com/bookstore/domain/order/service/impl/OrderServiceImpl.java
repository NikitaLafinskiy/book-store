package com.bookstore.domain.order.service.impl;

import com.bookstore.domain.order.dto.CreateOrderRequestDto;
import com.bookstore.domain.order.dto.OrderDto;
import com.bookstore.domain.order.dto.UpdateOrderStatusRequestDto;
import com.bookstore.domain.order.repository.OrderRepository;
import com.bookstore.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    @Override
    public OrderDto createOrder(CreateOrderRequestDto createOrderRequestDto) {
        return null;
    }

    @Override
    public List<OrderDto> getOrderHistory(String email) {
        return List.of();
    }

    @Override
    public OrderDto updateOrderStatus(Long orderId, UpdateOrderStatusRequestDto updateOrderStatusRequestDto) {
        return null;
    }
}
