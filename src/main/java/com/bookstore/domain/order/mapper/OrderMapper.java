package com.bookstore.domain.order.mapper;

import com.bookstore.config.MapperConfig;
import com.bookstore.domain.book.mapper.BookMapper;
import com.bookstore.domain.order.dto.CreateOrderItemRequestDto;
import com.bookstore.domain.order.dto.CreateOrderRequestDto;
import com.bookstore.domain.order.entity.Order;
import com.bookstore.domain.order.entity.OrderItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface OrderMapper {
    @Mapping(target = "orderItems", ignore = true)
    Order toOrderFromCreateOrderRequestDto(CreateOrderRequestDto
                                                   createOrderRequestDto);

    @AfterMapping
    default void setOrderItems(CreateOrderRequestDto createOrderRequestDto,
                               @MappingTarget Order order) {
        Set<OrderItem> orderItems = createOrderRequestDto.getOrderItems()
                .stream()
                .map(this::toOrderItemFromCreateOrderItemRequestDto)
                .peek(orderItem -> orderItem.setOrder(order))
                .collect(Collectors.toSet());
        order.setOrderItems(orderItems);
    }

    @Mapping(target = "book", source = "bookId", qualifiedByName = "bookById")
    OrderItem toOrderItemFromCreateOrderItemRequestDto(CreateOrderItemRequestDto
                                                               createOrderItemRequestDto);
}
