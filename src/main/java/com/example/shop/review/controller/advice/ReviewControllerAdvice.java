package com.example.shop.review.controller.advice;

import com.example.shop.review.exception.ReviewTaskException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

// 리뷰의 예외처리
@RestControllerAdvice
public class ReviewControllerAdvice {

    @ExceptionHandler(ReviewTaskException.class)
    public ResponseEntity<Map<String, String>> handleReviewTaskException(ReviewTaskException exception) {

        int status = exception.getCode();
        String message = exception.getMessage();

        return ResponseEntity.status(status).body(Map.of("message", message));
    }
}
