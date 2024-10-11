package com.bookstore.domain.order.mapper;

import com.bookstore.config.MapperConfig;
import com.bookstore.domain.book.mapper.BookMapper;
import com.bookstore.domain.order.dto.CreateOrderItemRequestDto;
import com.bookstore.domain.order.dto.CreateOrderRequestDto;
import com.bookstore.domain.order.dto.OrderDto;
import com.bookstore.domain.order.dto.OrderItemDto;
import com.bookstore.domain.order.dto.UpdateOrderStatusRequestDto;
import com.bookstore.domain.order.entity.Order;
import com.bookstore.domain.order.entity.OrderItem;
import com.bookstore.domain.user.entity.User;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface OrderMapper {
    @Mapping(target = "orderItems", ignore = true)
    Order toOrderFromDto(CreateOrderRequestDto createOrderRequestDto,
                         User user);

    @AfterMapping
    default void setOrderItems(CreateOrderRequestDto createOrderRequestDto,
                               @MappingTarget Order order) {
        Set<OrderItem> orderItems = createOrderRequestDto.getOrderItems()
                .stream()
                .map(this::toOrderItemFromDto)
                .peek(orderItem -> orderItem.setOrder(order))
                .collect(Collectors.toSet());
        BigDecimal total = orderItems.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setStatus(Order.Status.PENDING);
        order.setTotal(total);
        order.setOrderDate(LocalDateTime.now());
        order.setOrderItems(orderItems);
    }

    @Mapping(target = "book", source = "bookId", qualifiedByName = "bookById")
    OrderItem toOrderItemFromDto(CreateOrderItemRequestDto createOrderItemRequestDto);

    @Mapping(target = "orderItems", ignore = true)
    @Mapping(target = "userId", source = "user.id")
    OrderDto toDtoFromOrder(Order order);

    @AfterMapping
    default void setOrderDtoItems(Order order, @MappingTarget OrderDto orderDto) {
        Set<OrderItemDto> orderItemDtos = order.getOrderItems()
                .stream()
                .map(this::toDtoFromOrderItem)
                .collect(Collectors.toSet());
        orderDto.setOrderItems(orderItemDtos);
    }

    @Mapping(target = "bookId", source = "book.id")
    OrderItemDto toDtoFromOrderItem(OrderItem orderItem);

    void updateOrderFromDto(UpdateOrderStatusRequestDto updateOrderStatusRequestDto,
                            @MappingTarget Order order);
}
