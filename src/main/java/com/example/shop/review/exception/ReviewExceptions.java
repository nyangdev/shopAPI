package com.example.shop.review.exception;

// ReviewTaskException 을 enum 으로 정의함
public enum ReviewExceptions {

    REVIEW_NOT_REGISTRED("Review Not Registered", 400),
    REVIEW_PRODUCT_NOT_FOUND("Product Not Found for Review", 404),
    // 조회 예외
    REVIEW_NOT_FOUND("Review Not Found", 404),

    // 삭제와 수정 과정에서 예외
    REVIEW_NOT_MODIFIED("Review Not Modified", 400),
    REVIEW_NOT_REMOVED("Review Not Removed", 400);

    private final ReviewTaskException reviewTaskException;

    ReviewExceptions(String msg, int code) {

        reviewTaskException = new ReviewTaskException(msg, code);
    }

    public ReviewTaskException get() {
        return reviewTaskException;
    }
}
