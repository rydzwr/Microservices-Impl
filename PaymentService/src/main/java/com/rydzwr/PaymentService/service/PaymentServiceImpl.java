package com.rydzwr.PaymentService.service;

import com.rydzwr.PaymentService.entity.TransactionDetails;
import com.rydzwr.PaymentService.model.PaymentRequest;
import com.rydzwr.PaymentService.repository.TransactionDetailsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private TransactionDetailsRepository transactionDetailsRepository;

    @Override
    public int doPayment(PaymentRequest paymentRequest) {

        log.info("Recording Payment Details: {}", paymentRequest);

        var transactionDetails = TransactionDetails.builder()
                .referenceNumber(paymentRequest.getReferenceNumber())
                .paymentMode(paymentRequest.getPaymentMode().name())
                .orderId(paymentRequest.getOrderId())
                .amount(paymentRequest.getAmount())
                .paymentDate(Instant.now())
                .paymentStatus("SUCCESS")
                .build();

        transactionDetails = transactionDetailsRepository.save(transactionDetails);

        log.info("TRANSACTION COMPLETED WITH ID: {}", transactionDetails.getTransactionDetailsId());

        return transactionDetails.getTransactionDetailsId();
    }
}
