package com.bookstore.domain.order.controller;

import com.bookstore.domain.order.dto.CreateOrderRequestDto;
import com.bookstore.domain.order.dto.OrderDto;
import com.bookstore.domain.order.dto.UpdateOrderStatusRequestDto;
import com.bookstore.domain.order.service.OrderService;
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

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public OrderDto createOrder(@AuthenticationPrincipal String email,
                                @RequestBody @Valid
                                        CreateOrderRequestDto createOrderRequestDto) {
        return orderService.createOrder(email, createOrderRequestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public List<OrderDto> getOrderHistory(@AuthenticationPrincipal String email) {
        return orderService.getOrderHistory(email);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public OrderDto updateOrderStatus(@PathVariable("id") Long orderId,
                                      @RequestBody @Valid
                                      UpdateOrderStatusRequestDto updateOrderStatusRequestDto) {
        return orderService.updateOrderStatus(orderId, updateOrderStatusRequestDto);
    }
}
