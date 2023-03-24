package com.rydzwr.OrderService.service;

import com.rydzwr.OrderService.entity.Order;
import com.rydzwr.OrderService.extrenal.client.ProductService;
import com.rydzwr.OrderService.model.OrderRequest;
import com.rydzwr.OrderService.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Override
    public int placeOrder(OrderRequest orderRequest) {
        log.info("Placing Order Request {}", orderRequest);

        productService.reduceQuantity(orderRequest.getProductId(), orderRequest.getQuantity());

        log.info("Creating Order With Status CREATED");

        var order = Order.builder()
                .productsQuantity(orderRequest.getQuantity())
                .orderAmount(orderRequest.getTotalAmount())
                .productId(orderRequest.getProductId())
                .orderDate(Instant.now())
                .orderStatus("CREATED")
                .build();

        order = orderRepository.save(order);

        log.info("Order Placed Successfully With Order ID: {}", order.getOrderId());

        return order.getOrderId();
    }


}
