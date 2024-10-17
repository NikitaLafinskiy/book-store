package com.bookstore.controller;

import com.bookstore.dto.order.CreateOrderRequestDto;
import com.bookstore.dto.order.OrderDto;
import com.bookstore.dto.order.OrderItemDto;
import com.bookstore.dto.order.UpdateOrderStatusRequestDto;
import com.bookstore.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Orders", description = "Order management")
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "Create a new order", responses = {
            @ApiResponse(responseCode = "200", description = "Order created successfully")
    })
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public OrderDto createOrder(@AuthenticationPrincipal Long id,
                                @RequestBody @Valid
                                        CreateOrderRequestDto createOrderRequestDto) {
        return orderService.createOrder(id, createOrderRequestDto);
    }

    @Operation(summary = "Get order history", responses = {
            @ApiResponse(responseCode = "200", description = "Order history retrieved successfully")
    })
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public List<OrderDto> getOrderHistory(@AuthenticationPrincipal Long id) {
        return orderService.getOrderHistory(id);
    }

    @Operation(summary = "Update order status", responses = {
            @ApiResponse(responseCode = "200", description = "Order status updated successfully")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public OrderDto updateOrderStatus(@PathVariable("id") Long orderId,
                                      @RequestBody @Valid
                                      UpdateOrderStatusRequestDto updateOrderStatusRequestDto) {
        return orderService.updateOrderStatus(orderId, updateOrderStatusRequestDto);
    }

    @Operation(summary = "Get all order items", responses = {
            @ApiResponse(responseCode = "200", description = "Order items retrieved successfully")
    })
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{orderId}/items")
    public List<OrderItemDto> getOrderItems(
            @PathVariable("orderId") Long orderId,
            @AuthenticationPrincipal Long id) {
        return orderService.getOrderItems(orderId, id);
    }

    @Operation(summary = "Get an order item by ID", responses = {
            @ApiResponse(responseCode = "200", description = "Order item retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Order item not found")
    })
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{orderId}/items/{itemId}")
    public OrderItemDto getOrderItem(
            @PathVariable("itemId") Long itemId,
            @PathVariable("orderId") Long orderId,
            @AuthenticationPrincipal Long id) {
        return orderService.getOrderItem(itemId, orderId, id);
    }
}
