package com.bookstore.controller;

import com.bookstore.dto.order.OrderItemDto;
import com.bookstore.service.order.OrderItemService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "OrderItem", description = "Order Item API")
@RestController
@RequestMapping("/orders/{orderId}/items")
@RequiredArgsConstructor
public class OrderItemController {
    private final OrderItemService orderItemService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public List<OrderItemDto> getOrderItems(@PathVariable("orderId")
                                                Long orderId) {
        return orderItemService.getOrderItems(orderId);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{itemId}")
    public OrderItemDto getOrderItem(@PathVariable("itemId")
                                         Long itemId) {
        return orderItemService.getOrderItem(itemId);
    }
}
