package com.bookstore.service.order;

import com.bookstore.dto.order.CreateOrderRequestDto;
import com.bookstore.dto.order.OrderDto;
import com.bookstore.dto.order.OrderItemDto;
import com.bookstore.dto.order.UpdateOrderStatusRequestDto;
import com.bookstore.entity.Order;
import com.bookstore.entity.OrderItem;
import com.bookstore.entity.ShoppingCart;
import com.bookstore.entity.User;
import com.bookstore.mapper.OrderMapper;
import com.bookstore.repository.CartItemRepository;
import com.bookstore.repository.OrderItemRepository;
import com.bookstore.repository.OrderRepository;
import com.bookstore.repository.ShoppingCartRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    @Transactional
    public OrderDto createOrder(Long userId, CreateOrderRequestDto createOrderRequestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                "Shopping cart with an id of "
                        + userId
                        + "not found"));
        if (shoppingCart.getCartItems().isEmpty()) {
            throw new IllegalArgumentException(
                    "Shopping cart with an id of "
                    + userId
                    + "is empty");
        }
        User user = shoppingCart.getUser();
        Order order = orderMapper.toOrderFromDto(createOrderRequestDto,
                user,
                shoppingCart.getCartItems());
        orderRepository.save(order);
        OrderDto orderDto = orderMapper.toDtoFromOrder(order);
        cartItemRepository.deleteByShoppingCartId(shoppingCart.getId());
        return orderDto;
    }

    @Override
    public List<OrderDto> getOrderHistory(Long userId) {
        List<Order> orders = orderRepository.findAllByUserId(userId);
        return orders.stream()
                .map(orderMapper::toDtoFromOrder)
                .toList();
    }

    @Override
    public OrderDto updateOrderStatus(Long orderId,
                                      UpdateOrderStatusRequestDto updateOrderStatusRequestDto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        orderMapper.updateOrderFromDto(updateOrderStatusRequestDto, order);
        return orderMapper.toDtoFromOrder(orderRepository.save(order));
    }

    @Override
    public List<OrderItemDto> getOrderItems(Long orderId, Long userId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new com.bookstore.exception.EntityNotFoundException(
                        "Order not found"));
        return order.getOrderItems()
                .stream()
                .map(orderMapper::toDtoFromOrderItem)
                .toList();
    }

    @Override
    public OrderItemDto getOrderItem(Long orderItemId, Long orderId, Long userId) {
        OrderItem orderItem = orderItemRepository.findByIdAndOrderIdAndUserId(orderItemId,
                        orderId,
                        userId)
                .orElseThrow(() -> new com.bookstore.exception.EntityNotFoundException(
                        "Order item not found"));
        return orderMapper.toDtoFromOrderItem(orderItem);
    }
}
