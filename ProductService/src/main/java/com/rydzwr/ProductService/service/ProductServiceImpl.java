package com.rydzwr.ProductService.service;

import com.rydzwr.ProductService.entity.Product;
import com.rydzwr.ProductService.exception.ProductServiceCustomException;
import com.rydzwr.ProductService.model.ProductRequest;
import com.rydzwr.ProductService.model.ProductResponse;
import com.rydzwr.ProductService.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public int addProduct(ProductRequest productRequest) {
        log.info("Adding Product...");

        var product = Product.builder()
                .productName(productRequest.getProductName())
                .productPrice(productRequest.getProductPrice())
                .productQuantity(productRequest.getProductQuantity())
                .build();

        productRepository.save(product);

        log.info("Product Created");
        return product.getProductId();
    }

    @Override
    public ProductResponse getProductById(int productId) {
        log.info("get the product for productId: {}", productId);

        var product = productRepository
                .findById(productId)
                .orElseThrow(() -> new ProductServiceCustomException(
                        "Product With Given ID Not Found",
                        "PRODUCT NOT FOUND"
                ));

        return ProductResponse.builder()
                .productQuantity(product.getProductQuantity())
                .productPrice(product.getProductPrice())
                .productName(product.getProductName())
                .productId(product.getProductId())
                .build();
    }

    @Override
    public void reduceQuantity(int productId, int quantity) {
        log.info("Reduce Quantity: {}, for ID: {}", quantity, productId);

        var product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceCustomException(
                        "Product With Given ID Not Found",
                        "PRODUCT_NOT_FOUND"
                ));

        if (product.getProductQuantity() < quantity) {
            throw new ProductServiceCustomException(
                    "Product Does not Have Sufficient Quantity",
                    "INSUFFICIENT_QUANTITY"
            );
        }

        product.setProductQuantity(product.getProductQuantity() - quantity);
        productRepository.save(product);

        log.info("Product Quantity Updated Successfully");
    }
}
