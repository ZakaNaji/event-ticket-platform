package com.znaji.event_ticket_platform.api.orders;

import com.znaji.event_ticket_platform.api.orders.io.CreateOrderRequest;
import com.znaji.event_ticket_platform.api.orders.io.OrderResponse;
import com.znaji.event_ticket_platform.application.orders.OrderApplicationService;
import com.znaji.event_ticket_platform.application.orders.commands.CreateOrderCommand;
import com.znaji.event_ticket_platform.common.mapping.orders.OrderMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@Validated
public class OrderController {

    private final OrderApplicationService orderApplicationService;

    public OrderController(OrderApplicationService orderApplicationService) {
        this.orderApplicationService = orderApplicationService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
       CreateOrderCommand command = OrderMapper.toCommand(request);
        OrderResponse order = orderApplicationService.createOrder(command);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(order);
    }

    @PutMapping("/{orderId}/confirm")
    public ResponseEntity<Void> confirmOrder(@PathVariable @NotNull UUID orderId) {
        orderApplicationService.confirmOrder(orderId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable @NotNull UUID orderId) {
        orderApplicationService.cancelOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}
