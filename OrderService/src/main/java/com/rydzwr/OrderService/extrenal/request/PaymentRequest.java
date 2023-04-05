package com.rydzwr.OrderService.extrenal.request;

import com.rydzwr.OrderService.model.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    private int orderId;
    private int amount;
    private String referenceNumber;
    private PaymentMode paymentMode;
}
