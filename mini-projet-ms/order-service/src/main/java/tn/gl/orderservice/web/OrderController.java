package tn.gl.orderservice.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.gl.orderservice.domain.Order;
import tn.gl.orderservice.dto.CreateOrderDTO;
import tn.gl.orderservice.dto.OrderDetailsDTO;
import tn.gl.orderservice.service.OrderService;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    
    private final OrderService orderService;

    @PostMapping
    public Order createOrder(@RequestBody CreateOrderDTO dto) {
        return orderService.createOrder(dto);
    }

    @GetMapping("/{id}")
    public Order getOrder(@PathVariable Long id) {
        // Simple default view
        return null; // Implemented in service but not exposed here explicitly in prompt except details
    }

    @GetMapping("/{id}/details")
    public OrderDetailsDTO getOrderDetails(@PathVariable Long id) {
        return orderService.getOrderDetails(id);
    }
}
