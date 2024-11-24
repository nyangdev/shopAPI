package com.example.shop.cart.controller.advice;

import com.example.shop.cart.exception.CartTaskException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class CartControllerAdvice {

    @ExceptionHandler(CartTaskException.class)
    public ResponseEntity<Map<String, String>> handleCartTaskException(CartTaskException e) {

        int status = e.getStatus();
        String message = e.getMessage();

        return ResponseEntity.status(status).body(Map.of("error", message));
    }
}
