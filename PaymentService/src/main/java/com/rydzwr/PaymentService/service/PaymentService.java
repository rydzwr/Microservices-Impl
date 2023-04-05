package com.rydzwr.PaymentService.service;

import com.rydzwr.PaymentService.model.PaymentRequest;
import com.rydzwr.PaymentService.model.PaymentResponse;

public interface PaymentService {
    int doPayment(PaymentRequest paymentRequest);

    PaymentResponse getPaymentDetailsByOrderId(int orderId);
}
