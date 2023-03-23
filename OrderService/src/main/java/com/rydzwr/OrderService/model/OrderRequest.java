package com.rydzwr.OrderService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private int productId;
    private int totalAmount;
    private int quantity;
    private PaymentMode paymentMode;
}
