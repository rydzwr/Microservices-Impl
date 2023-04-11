package com.rydzwr.OrderService.service;

import com.rydzwr.OrderService.entity.Order;
import com.rydzwr.OrderService.exception.CustomException;
import com.rydzwr.OrderService.extrenal.client.PaymentService;
import com.rydzwr.OrderService.extrenal.client.ProductService;
import com.rydzwr.OrderService.extrenal.request.PaymentRequest;
import com.rydzwr.OrderService.extrenal.response.PaymentResponse;
import com.rydzwr.OrderService.extrenal.response.ProductResponse;
import com.rydzwr.OrderService.model.OrderRequest;
import com.rydzwr.OrderService.model.PaymentMode;
import com.rydzwr.OrderService.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductService productService;

    @Mock
    private PaymentService paymentService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    OrderService orderService = new OrderServiceImpl();

    @Test
    @DisplayName("Get Order - Success Scenario")
    void test_When_Order_Success() {

        //Mocking
        Order order = getMockOrder();

        when(orderRepository.findById(anyInt()))
                .thenReturn(Optional.of(order));

        when(restTemplate.getForObject(
                "http://PRODUCT-SERVICE/product/" + order.getProductId(),
                ProductResponse.class)).thenReturn(getMockProductResponse()
        );

        when(restTemplate.getForObject(
                "http://PAYMENT-SERVICE/payment/order/" + order.getOrderId(),
                PaymentResponse.class)).thenReturn(getMockPaymentResponse()
        );

        //Actual
        var orderResponse = orderService.getOrderDetails(1);

        //Verification
        verify(orderRepository, times(1))
                .findById(anyInt());

        verify(restTemplate, times(1))
                .getForObject("http://PRODUCT-SERVICE/product/" + order.getProductId(),
                        ProductResponse.class
                );


        verify(restTemplate, times(1))
                .getForObject("http://PAYMENT-SERVICE/payment/order/" + order.getOrderId(),
                        PaymentResponse.class
                );

        //Assert
        assertNotNull(orderResponse);
        assertEquals(order.getOrderId(), orderResponse.getOrderId());
    }
    @DisplayName("Get Orders - Failure Scenario")
    @Test
    void test_When_Get_Order_NOT_FOUND_then_Not_Found() {

        when(orderRepository.findById(anyInt()))
                .thenReturn(Optional.ofNullable(null));

        var exception =
                assertThrows(CustomException.class,
                        () -> orderService.getOrderDetails(1));
        assertEquals("NOT_FOUND", exception.getErrorCode());
        assertEquals(404, exception.getStatus());

        verify(orderRepository, times(1))
                .findById(anyInt());
    }

    @DisplayName("Place Order - Success Scenario")
    @Test
    void test_When_Place_Order_Success() {
        Order order = getMockOrder();
        var orderRequest = getMockOrderRequest();

        when(orderRepository.save(any(Order.class)))
                .thenReturn(order);
        when(productService.reduceQuantity(anyInt(),anyInt()))
                .thenReturn(new ResponseEntity<Void>(HttpStatus.OK));
        when(paymentService.doPayment(any(PaymentRequest.class)))
                .thenReturn(new ResponseEntity<Integer>(1, HttpStatus.OK));

        long orderId = orderService.placeOrder(orderRequest);

        verify(orderRepository, times(2))
                .save(any());
        verify(productService, times(1))
                .reduceQuantity(anyInt(),anyInt());
        verify(paymentService, times(1))
                .doPayment(any(PaymentRequest.class));

        assertEquals(order.getOrderId(), orderId);
    }

    @DisplayName("Place Order - Payment Failed Scenario")
    @Test
    void test_when_Place_Order_Payment_Fails_then_Order_Placed() {

        Order order = getMockOrder();
        var orderRequest = getMockOrderRequest();

        when(orderRepository.save(any(Order.class)))
                .thenReturn(order);
        when(productService.reduceQuantity(anyInt(), anyInt()))
                .thenReturn(new ResponseEntity<Void>(HttpStatus.OK));
        when(paymentService.doPayment(any(PaymentRequest.class)))
                .thenThrow(new RuntimeException());

        long orderId = orderService.placeOrder(orderRequest);

        verify(orderRepository, times(2))
                .save(any());
        verify(productService, times(1))
                .reduceQuantity(anyInt(), anyInt());
        verify(paymentService, times(1))
                .doPayment(any(PaymentRequest.class));

        assertEquals(order.getOrderId(), orderId);
    }

    private OrderRequest getMockOrderRequest() {
        return OrderRequest.builder()
                .paymentMode(PaymentMode.CASH)
                .totalAmount(100)
                .quantity(10)
                .productId(1)
                .build();
    }

    private PaymentResponse getMockPaymentResponse() {
        return PaymentResponse.builder()
                .paymentMode(PaymentMode.CASH)
                .paymentDate(Instant.now())
                .status("ACCEPTED")
                .paymentId(1)
                .amount(200)
                .orderId(1)
                .build();
    }

    private ProductResponse getMockProductResponse() {
        return ProductResponse.builder()
                .productName("iPhone")
                .productQuantity(200)
                .productPrice(100)
                .productId(2)
                .build();
    }

    private Order getMockOrder() {
        return Order.builder()
                .orderDate(Instant.now())
                .productsQuantity(200)
                .orderStatus("PLACED")
                .orderAmount(100)
                .productId(2)
                .orderId(1)
                .build();
    }
}