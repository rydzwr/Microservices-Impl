package com.rydzwr.ProductService.model;

import lombok.Data;

@Data
public class ProductRequest {
    private String productName;
    private int productPrice;
    private int productQuantity;
}
