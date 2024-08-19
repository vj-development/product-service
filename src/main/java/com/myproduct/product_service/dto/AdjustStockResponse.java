package com.myproduct.product_service.dto;

import com.myproduct.product_service.models.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AdjustStockResponse {
    private List<Product> products;
    private String statusMessage;
    private int errorCode;
}
