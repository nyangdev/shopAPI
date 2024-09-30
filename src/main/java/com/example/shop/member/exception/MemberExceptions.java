package com.example.shop.member.exception;

public enum MemberExceptions {

    // 요청한 바를 찾을 수 없음
    NOT_FOUND("NOT_FOUND", 404),

    // 중복
    DUPLICATE("DUPLICATE", 409),

    // Bad Request
    INVALID("INVALID", 400);

    private MemberTaskException memberTaskException;

    MemberExceptions(String msg, int code) {
        memberTaskException = new MemberTaskException(msg, code);
    }

    public MemberTaskException get() {
        return memberTaskException;
    }
}
