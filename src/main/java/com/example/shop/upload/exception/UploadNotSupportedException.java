package com.example.shop.upload.exception;

public class UploadNotSupportedException extends RuntimeException {

    // 파일 확장자 예외 처리
    public UploadNotSupportedException(String message) {
        super(message);
    }
}
