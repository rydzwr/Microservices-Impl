package com.rydzwr.ProductService.service;

import com.rydzwr.ProductService.model.ProductRequest;
import com.rydzwr.ProductService.model.ProductResponse;

public interface ProductService {
    int addProduct(ProductRequest productRequest);
    ProductResponse getProductById(int productId);

    void reduceQuantity(int productId, int quantity);
}
