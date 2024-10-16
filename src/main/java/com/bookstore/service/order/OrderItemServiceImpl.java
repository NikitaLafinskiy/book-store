package com.bookstore.service.order;

import com.bookstore.dto.order.OrderItemDto;
import com.bookstore.entity.Order;
import com.bookstore.entity.OrderItem;
import com.bookstore.exception.EntityNotFoundException;
import com.bookstore.mapper.OrderMapper;
import com.bookstore.repository.OrderItemRepository;
import com.bookstore.repository.OrderRepository;
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
