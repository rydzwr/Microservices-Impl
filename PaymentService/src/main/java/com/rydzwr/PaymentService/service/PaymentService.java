package com.rydzwr.PaymentService.service;

import com.rydzwr.PaymentService.model.PaymentRequest;

public interface PaymentService {
    int doPayment(PaymentRequest paymentRequest);
}
