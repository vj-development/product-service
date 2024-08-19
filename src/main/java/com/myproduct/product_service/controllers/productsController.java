package com.myproduct.product_service.controllers;

import com.myproduct.product_service.dto.AdjustStockRequest;
import com.myproduct.product_service.dto.AdjustStockResponse;
import com.myproduct.product_service.exceptions.InvalidSkuException;
import com.myproduct.product_service.models.Product;
import com.myproduct.product_service.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class productsController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<Iterable<Product>> index(){
        return ResponseEntity.ok(productService.products());
    }

    @PostMapping("/products/create")
    public ResponseEntity<Product> create(Product product){
        return ResponseEntity.ok(productService.create(product));
    }

    @PostMapping("/products/bulk_create")
    public ResponseEntity<List<Product>> bulkCreate(@RequestBody List<Product> products) {
        List<Product> createdProducts = new ArrayList<>();
        for (Product product : products) {
            createdProducts.add(productService.create(product));
        }
        return ResponseEntity.ok(createdProducts);
    }

    @GetMapping("/products/{sku}/stock/{stockLevel}")
    public ResponseEntity<Product> getProductWithStock(@PathVariable String sku, @PathVariable Integer stockLevel){
        Optional<Product> optionalProduct = productService.show(sku, stockLevel);
        if (optionalProduct.isPresent()) {
            return ResponseEntity.ok(optionalProduct.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/products/{sku}")
    public ResponseEntity<Product> show(@PathVariable String sku){
        Optional<Product> optionalProduct = productService.show(sku, 0);
        if (optionalProduct.isPresent()) {
            return ResponseEntity.ok(optionalProduct.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/products/in_stock")
    public ResponseEntity<Iterable<Product>> inStockProducts(){
        return ResponseEntity.ok(productService.InStockProducts());
    }

//    @Autowired
    @PostMapping("/products/adjust_stock")
    public ResponseEntity<AdjustStockResponse> adjustStock(@RequestBody List<AdjustStockRequest> adjustStockRequests) throws InvalidSkuException {
        try{
            return ResponseEntity.ok(productService.adjustStock(adjustStockRequests));
        }catch(Exception e){
            AdjustStockResponse asp = new AdjustStockResponse();
            asp.setStatusMessage("Error: "+e.getMessage());
            asp.setErrorCode(400);
            return ResponseEntity.badRequest().body(asp);
        }

    }

}
