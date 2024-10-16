package com.bookstore.service.order;

import com.bookstore.dto.book.BookPriceProjectionDto;
import com.bookstore.dto.order.CreateOrderItemRequestDto;
import com.bookstore.dto.order.CreateOrderRequestDto;
import com.bookstore.dto.order.OrderDto;
import com.bookstore.dto.order.UpdateOrderStatusRequestDto;
import com.bookstore.entity.Order;
import com.bookstore.entity.User;
import com.bookstore.mapper.OrderMapper;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.OrderRepository;
import com.bookstore.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private final BookRepository bookRepository;

    @Override
    public OrderDto createOrder(String email, CreateOrderRequestDto createOrderRequestDto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);
        List<BookPriceProjectionDto> books = bookRepository.findAllByBookIds(
                createOrderRequestDto.getOrderItems()
                .stream()
                .map(CreateOrderItemRequestDto::getBookId)
                .toList());
        Order order = orderMapper.toOrderFromDto(createOrderRequestDto,
                user,
                books);
        return orderMapper.toDtoFromOrder(orderRepository.save(order));
    }

    @Override
    public List<OrderDto> getOrderHistory(String email) {
        List<Order> orders = orderRepository.findByUserEmail(email);
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
