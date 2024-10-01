package com.example.shop.member.exception;

public enum MemberExceptions {

    // 요청한 바를 찾을 수 없음
    NOT_FOUND("NOT_FOUND", 404),

    // 중복
    DUPLICATE("DUPLICATE", 409),

    // Bad Request
    INVALID("INVALID", 400),

    // 잘못된 인증 정보
    // 토큰 발행을 위한 사용자의 정보 확인 인증 과정에서 발생하는 문제 처리
    BAD_CREDENTIALS("BAD_CREDENTIALS", 401);

    private MemberTaskException memberTaskException;

    MemberExceptions(String msg, int code) {
        memberTaskException = new MemberTaskException(msg, code);
    }

    public MemberTaskException get() {
        return memberTaskException;
    }
}
