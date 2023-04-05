package com.rydzwr.OrderService.service;

import com.netflix.discovery.converters.Auto;
import com.rydzwr.OrderService.entity.Order;
import com.rydzwr.OrderService.exception.CustomException;
import com.rydzwr.OrderService.extrenal.client.PaymentService;
import com.rydzwr.OrderService.extrenal.client.ProductService;
import com.rydzwr.OrderService.extrenal.request.PaymentRequest;
import com.rydzwr.OrderService.extrenal.response.PaymentResponse;
import com.rydzwr.OrderService.model.OrderRequest;
import com.rydzwr.OrderService.model.OrderResponse;
import com.rydzwr.OrderService.repository.OrderRepository;
import com.rydzwr.ProductService.model.ProductResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public int placeOrder(OrderRequest orderRequest) {
        log.info("Placing Order Request {}", orderRequest);

        productService.reduceQuantity(orderRequest.getProductId(), orderRequest.getQuantity());

        log.info("Creating Order With Status CREATED");

        var order = Order.builder()
                .productsQuantity(orderRequest.getQuantity())
                .orderAmount(orderRequest.getTotalAmount())
                .productId(orderRequest.getProductId())
                .orderDate(Instant.now())
                .orderStatus("CREATED")
                .build();

        order = orderRepository.save(order);

        log.info("Calling Payment Service To Complete The Payment");

        var paymentRequest = PaymentRequest.builder()
                .orderId(order.getOrderId())
                .paymentMode(orderRequest.getPaymentMode())
                .amount(orderRequest.getTotalAmount())
                .build();

        String orderStatus = null;
        try {
            paymentService.doPayment(paymentRequest);
            log.info("Payment Done Successfully. Changing The Order Status To PLACED");
            orderStatus = "PLACED";
        } catch (Exception e) {
            log.error("Error Occurred In Payment. Changing The Order Status To FAILED");
            orderStatus = "FAILED";
        }

        order.setOrderStatus(orderStatus);
        orderRepository.save(order);

        log.info("Order Placed Successfully With Order ID: {}", order.getOrderId());

        return order.getOrderId();
    }

    @Override
    public OrderResponse getOrderDetails(int orderId) {
        log.info("Get Order Details For Order ID : {}", orderId);

        var order = orderRepository.findById(orderId).orElseThrow(
                () -> new CustomException("Order Not Found For Order ID:" + orderId, "NOT_FOUND", 404));

        log.info("Invoking Product Service To Fetch The Product Response");
        var productResponse = restTemplate.getForObject("http://PRODUCT-SERVICE/product/" + order.getProductId(),
                ProductResponse.class);

        log.info("getting Payment Information For The Payment Service");
        var paymentResponse = restTemplate.getForObject("http://PAYMENT-SERVICE/payment/order/" + order.getOrderId(),
                PaymentResponse.class);

        var productDetails = OrderResponse.ProductDetails.builder()
                .productName(productResponse.getProductName())
                .productId(productResponse.getProductId())
                .build();

        var paymentDetails = OrderResponse.PaymentDetails.builder()
                .paymentMode(paymentResponse.getPaymentMode())
                .paymentDate(paymentResponse.getPaymentDate())
                .paymentStatus(paymentResponse.getStatus())
                .paymentId(paymentResponse.getPaymentId())
                .build();

        return OrderResponse.builder()
                .orderStatus(order.getOrderStatus())
                .orderDate(order.getOrderDate())
                .paymentDetails(paymentDetails)
                .productDetails(productDetails)
                .amount(order.getOrderAmount())
                .orderId(order.getOrderId())
                .build();
    }
}
