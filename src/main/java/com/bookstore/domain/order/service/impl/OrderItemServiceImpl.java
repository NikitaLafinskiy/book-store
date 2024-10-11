package com.bookstore.domain.order.service.impl;

import com.bookstore.domain.order.dto.OrderItemDto;
import com.bookstore.domain.order.entity.Order;
import com.bookstore.domain.order.entity.OrderItem;
import com.bookstore.domain.order.mapper.OrderMapper;
import com.bookstore.domain.order.repository.OrderItemRepository;
import com.bookstore.domain.order.repository.OrderRepository;
import com.bookstore.domain.order.service.OrderItemService;
import com.bookstore.exception.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;

    @Override
    public List<OrderItemDto> getOrderItems(Long orderId) {
        Order order = orderRepository.findByIdWithOrderItems(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        return order.getOrderItems()
                .stream()
                .map(orderMapper::toDtoFromOrderItem)
                .toList();
    }

    @Override
    public OrderItemDto getOrderItem(Long orderItemId) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new EntityNotFoundException("Order item not found"));
        return orderMapper.toDtoFromOrderItem(orderItem);
    }
}
