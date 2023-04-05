package com.rydzwr.OrderService.extrenal.response;

import com.rydzwr.OrderService.model.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private int paymentId;
    private String status;
    private PaymentMode paymentMode;
    private int amount;
    private Instant paymentDate;
    private int orderId;
}
