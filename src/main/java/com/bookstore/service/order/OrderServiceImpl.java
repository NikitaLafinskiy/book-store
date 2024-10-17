package com.bookstore.service.order;

import com.bookstore.dto.order.CreateOrderRequestDto;
import com.bookstore.dto.order.OrderDto;
import com.bookstore.dto.order.UpdateOrderStatusRequestDto;
import com.bookstore.entity.Order;
import com.bookstore.entity.ShoppingCart;
import com.bookstore.entity.User;
import com.bookstore.mapper.OrderMapper;
import com.bookstore.repository.CartItemRepository;
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

    @Override
    @Transactional
    public OrderDto createOrder(String userId, CreateOrderRequestDto createOrderRequestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(Long.valueOf(userId))
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
        OrderDto orderDto = orderMapper.toDtoFromOrder(orderRepository.save(order));
        cartItemRepository.deleteAll(shoppingCart.getCartItems());
        return orderDto;
    }

    @Override
    public List<OrderDto> getOrderHistory(String userId) {
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
}
