package com.bookstore.mapper;

import com.bookstore.config.MapperConfig;
import com.bookstore.dto.book.BookPriceProjectionDto;
import com.bookstore.dto.order.CreateOrderItemRequestDto;
import com.bookstore.dto.order.CreateOrderRequestDto;
import com.bookstore.dto.order.OrderDto;
import com.bookstore.dto.order.OrderItemDto;
import com.bookstore.dto.order.UpdateOrderStatusRequestDto;
import com.bookstore.entity.Order;
import com.bookstore.entity.OrderItem;
import com.bookstore.entity.User;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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
                         List<BookPriceProjectionDto> books);

    @AfterMapping
    default void setOrderItems(CreateOrderRequestDto createOrderRequestDto,
                               List<BookPriceProjectionDto> books,
                               @MappingTarget Order order) {
        Map<Long, BigDecimal> bookPrices = books.stream()
                .collect(Collectors.toMap(BookPriceProjectionDto::id,
                        BookPriceProjectionDto::price));

        Set<OrderItem> orderItems = createOrderRequestDto.getOrderItems()
                .stream()
                .map(createOrderItemRequestDto -> {
                    OrderItem orderItem = toOrderItemFromDto(createOrderItemRequestDto);
                    orderItem.setOrder(order);
                    BigDecimal price = bookPrices.get(orderItem.getBook()
                            .getId());
                    if (price == null) {
                        throw new IllegalArgumentException("Book price not found for book "
                                + orderItem.getBook()
                                .getId());
                    }
                    orderItem.setPrice(price);
                    return orderItem;
                })
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
