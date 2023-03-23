package com.rydzwr.OrderService.service;

import com.rydzwr.OrderService.model.OrderRequest;

public interface OrderService {
    int placeOrder(OrderRequest orderRequest);
}
