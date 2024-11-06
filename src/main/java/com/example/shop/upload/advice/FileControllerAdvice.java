package com.example.shop.upload.advice;

// FileController 관련해서 예외 발생시 처리

import com.example.shop.upload.exception.EmptyFileException;
import com.example.shop.upload.exception.UploadNotSupportedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.Map;

@RestControllerAdvice
public class FileControllerAdvice {

    // 하나의 파일이 3MB가 넘는 경우, 업로드되는 전체 파일이 30MB보다 큰 경우
    // 상태코드 400
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxSizeException(MaxUploadSizeExceededException exception) {
        return ResponseEntity.badRequest().body(Map.of("error", "File too large"));
    }

    // 파일 확장자 예외 처리
    @ExceptionHandler(UploadNotSupportedException.class)
    public ResponseEntity<Map<String, String>> handleUploadNotSupportedException(UploadNotSupportedException exception) {
        return ResponseEntity.badRequest().body(Map.of("error", exception.getMessage()));
    }

    @ExceptionHandler(EmptyFileException.class)
    public ResponseEntity<Map<String, String>> handleEmptyFileException(EmptyFileException exception) {
        return ResponseEntity.badRequest().body(Map.of("error", exception.getMessage()));
    }
}
