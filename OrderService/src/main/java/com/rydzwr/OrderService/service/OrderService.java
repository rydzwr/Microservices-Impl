package com.rydzwr.OrderService.service;

import com.rydzwr.OrderService.model.OrderRequest;
import com.rydzwr.OrderService.model.OrderResponse;

public interface OrderService {
    int placeOrder(OrderRequest orderRequest);

    OrderResponse getOrderDetails(int orderId);
}
