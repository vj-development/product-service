package com.myproduct.product_service.dto;


import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

//@Component
@Getter
@Setter
public class AdjustStockRequest {

    private String sku;
    private Integer quantity;
}
