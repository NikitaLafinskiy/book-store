package com.bookstore.mapper;

import com.bookstore.config.MapperConfig;
import com.bookstore.dto.order.CreateOrderRequestDto;
import com.bookstore.dto.order.OrderDto;
import com.bookstore.dto.order.OrderItemDto;
import com.bookstore.dto.order.UpdateOrderStatusRequestDto;
import com.bookstore.entity.CartItem;
import com.bookstore.entity.Order;
import com.bookstore.entity.OrderItem;
import com.bookstore.entity.User;
import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface OrderMapper {
    @Mapping(target = "orderItems", ignore = true)
    @Mapping(target = "shippingAddress", source = "createOrderRequestDto.shippingAddress")
    @Mapping(target = "user", source = "user")
    Order toOrderFromDto(CreateOrderRequestDto createOrderRequestDto,
                         User user,
                         Set<CartItem> cartItems);

    @AfterMapping
    default void setOrderItems(Set<CartItem> cartItems,
                               @MappingTarget Order order) {
        Set<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> {
                    OrderItem orderItem = toOrderItemFromCartItem(cartItem);
                    orderItem.setOrder(order);
                    return orderItem;
                })
                .collect(Collectors.toSet());

        BigDecimal total = orderItems.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotal(total);
        order.setOrderItems(orderItems);
    }

    @Mapping(target = "price", source = "cartItem.book.price")
    OrderItem toOrderItemFromCartItem(CartItem cartItem);

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
