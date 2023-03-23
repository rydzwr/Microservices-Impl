package com.rydzwr.OrderService.controller;

import com.rydzwr.OrderService.model.OrderRequest;
import com.rydzwr.OrderService.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/placeOrder")
    public ResponseEntity<Integer> placeOrder(@RequestBody OrderRequest orderRequest) {
        var orderId = orderService.placeOrder(orderRequest);
        log.info("Order Id: {}", orderId);

        return new ResponseEntity<>(orderId, HttpStatus.OK);
    }
}
