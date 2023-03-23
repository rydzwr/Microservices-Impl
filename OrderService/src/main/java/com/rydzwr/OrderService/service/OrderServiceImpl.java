package com.rydzwr.OrderService.service;

import com.rydzwr.OrderService.entity.Order;
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

    @Override
    public int placeOrder(OrderRequest orderRequest) {
        log.info("Placing Order Request {}", orderRequest);

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
