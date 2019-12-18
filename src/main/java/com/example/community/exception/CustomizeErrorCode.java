package com.example.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {

    QUESTION_NOT_FOUND("所选问题为空,请重试");

    private String message;

    CustomizeErrorCode(String message) {
        this.message=message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
