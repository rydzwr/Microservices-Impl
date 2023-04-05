package com.rydzwr.CloudGateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

    @GetMapping("/orderServiceFallBack")
    public String orderServiceFallback() {
        return "Order Service Is Down";
    }

    @GetMapping("/paymentServiceFallBack")
    public String paymentServiceFallback() {
        return "Payment Service Is Down";
    }

    @GetMapping("/productServiceFallBack")
    public String productServiceFallback() {
        return "Product Service Is Down";
    }
}
