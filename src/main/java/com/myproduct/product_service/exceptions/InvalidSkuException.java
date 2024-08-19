package com.myproduct.product_service.exceptions;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvalidSkuException extends Throwable {
    private String message;
    public InvalidSkuException(String s) {
        setMessage(s);
    }
}
