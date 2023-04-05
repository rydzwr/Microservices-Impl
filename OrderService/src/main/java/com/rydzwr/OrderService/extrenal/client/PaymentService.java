package com.rydzwr.OrderService.extrenal.client;

import com.rydzwr.OrderService.exception.CustomException;
import com.rydzwr.OrderService.extrenal.request.PaymentRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CircuitBreaker(name = "external", fallbackMethod = "fallback")
@FeignClient(name = "PAYMENT-SERVICE/payment")
public interface PaymentService {

    @PostMapping
    public ResponseEntity<Integer> doPayment(@RequestBody PaymentRequest paymentRequest);

    default void fallback(Exception e) {
        throw new CustomException("Payment Service Is Not Available", "UNAVAILABLE", 500);
    }
}
