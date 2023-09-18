package org.shop.app.controller;

import org.shop.app.dto.CreateOrderDto;
import org.shop.app.dto.ViewOrderDto;
import org.shop.app.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/add-order")
    @PreAuthorize("hasRole('MODERATOR')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String createAnOrder(@RequestBody @Valid CreateOrderDto createOrderDto) {
        return orderService.createAnOrder(createOrderDto);
    }

    @GetMapping("/orders")
    @PreAuthorize("hasRole('USER') OR hasRole('MODERATOR')")
    public List<ViewOrderDto> viewAllAvailableOrders() {
        return orderService.viewAllAvailableOrders();
    }

}
